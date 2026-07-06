import streamlit as st
import pandas as pd
from datetime import date


# ==========================================
# 1. CAPA DE LÓGICA DE NEGOCIO (PYTHON PURO)
# ==========================================

class Transaccion:
    def __init__(self, descripcion, monto, fecha, categoria, tipo):
        self.descripcion = str(descripcion)
        self.monto = float(monto)
        self.fecha = fecha
        self.categoria = str(categoria)
        self.tipo = str(tipo)

    def es_gasto(self):
        return self.tipo == "Gasto"

    def es_ingreso(self):
        return self.tipo == "Ingreso"

    def a_diccionario(self):
        return {
            "descripcion": self.descripcion,
            "monto": self.monto,
            "fecha": self.fecha,
            "categoria": self.categoria,
            "tipo": self.tipo
        }


class Cartera:
    def __init__(self):
        self.transacciones = []

    def agregar_transaccion(self, transaccion):
        self.transacciones.append(transaccion)

    def obtener_ingresos(self, lista_txs=None):
        txs = lista_txs if lista_txs is not None else self.transacciones
        return sum(t.monto for t in txs if t.es_ingreso())

    def obtener_gastos(self, lista_txs=None):
        txs = lista_txs if lista_txs is not None else self.transacciones
        return sum(t.monto for t in txs if t.es_gasto())

    def obtener_balance(self, lista_txs=None):
        return self.obtener_ingresos(lista_txs) - self.obtener_gastos(lista_txs)

    def obtener_gasto_promedio(self, lista_txs=None):
        txs = lista_txs if lista_txs is not None else self.transacciones
        gastos = [t.monto for t in txs if t.es_gasto()]
        if not gastos:
            return 0.0
        return sum(gastos) / len(gastos)

    def filtrar(self, categorias_sel, desde, hasta):
        return [
            t for t in self.transacciones
            if t.categoria in categorias_sel and desde <= t.fecha <= hasta
        ]

    def agrupar_gastos_por_categoria(self, lista_txs=None):
        txs = lista_txs if lista_txs is not None else self.transacciones
        agrupado = {}
        for t in txs:
            if t.es_gasto():
                agrupado[t.categoria] = agrupado.get(t.categoria, 0.0) + t.monto
        return agrupado

    def agrupar_gastos_por_fecha(self, lista_txs=None):
        txs = lista_txs if lista_txs is not None else self.transacciones
        agrupado = {}
        for t in txs:
            if t.es_gasto():
                agrupado[t.fecha] = agrupado.get(t.fecha, 0.0) + t.monto
        return agrupado

    def guardar_csv(self, ruta_archivo):
        if self.transacciones:
            df = pd.DataFrame([t.a_diccionario() for t in self.transacciones])
            df.to_csv(ruta_archivo, index=False)
        else:
            df = pd.DataFrame(columns=["descripcion", "monto", "fecha", "categoria", "tipo"])
            df.to_csv(ruta_archivo, index=False)

    def cargar_csv(self, ruta_archivo):
        try:
            df = pd.read_csv(ruta_archivo)
            for _, fila in df.iterrows():
                t = Transaccion(
                    descripcion=fila["descripcion"],
                    monto=fila["monto"],
                    fecha=date.fromisoformat(str(fila["fecha"]).strip()),
                    categoria=fila["categoria"],
                    tipo=fila["tipo"]
                )
                self.agregar_transaccion(t)
        except Exception:
            pass  # Si no existe o falla, la cartera queda vacía


# ==========================================
# 2. CAPA DE PRESENTACIÓN E INTERFAZ (UI)
# ==========================================

ARCHIVO_DATOS = "transacciones.csv"
categorias = ["Comidas", "Transporte", "Salud", "Ocio", "Hogar", "Otros"]

st.caption("versión 1.0")
st.title("Tracker de Finanazas Personales")
st.write("Lleva el control de tus gastos e ingresos de manera simple y visual")


def inicializar_estado():
    if "cartera" not in st.session_state:
        cartera = Cartera()
        cartera.cargar_csv(ARCHIVO_DATOS)
        st.session_state.cartera = cartera


def mostrar_formulario():
    with st.form("nueva_transaccion: "):
        descripcion = st.text_input("Descripcion del gasto o ingreso", placeholder="Descripcion del gasto o ingreso")
        monto = st.number_input("Monto", step=1.0, min_value=0.0, format="%.2f")
        fecha = st.date_input("Fecha")
        categoria = st.selectbox("Categoria", categorias)
        tipo = st.radio("Tipo", ["Ingreso", "Gasto"], horizontal=True)
        enviado = st.form_submit_button("Enviar")

    if enviado:
        nueva_tx = Transaccion(descripcion, monto, fecha, categoria, tipo)
        st.session_state.cartera.agregar_transaccion(nueva_tx)
        st.success("exitoso en pesos")


def mostrar_filtros():
    cats_sel = st.multiselect("categorias", options=categorias, default=categorias)

    todas_txs = st.session_state.cartera.transacciones
    if todas_txs:
        fechas = [t.fecha for t in todas_txs]
        fecha_min = min(fechas)
        fecha_max = max(fechas)
    else:
        fecha_min = date.today()
        fecha_max = date.today()

    rango = st.date_input("Rango de fechas", value=(fecha_min, fecha_max))

    if isinstance(rango, (tuple, list)):
        desde = rango[0] if len(rango) > 0 else fecha_min
        hasta = rango[1] if len(rango) > 1 else desde
    else:
        desde = rango
        hasta = rango

    return cats_sel, desde, hasta


def importar_csv():
    with st.expander("importar desde CSV"):
        archivo = st.file_uploader("Selecciona un archivo CSV", type=["csv"])
        btn_importar = st.button("importar transacciones")

        if btn_importar:
            if archivo is None:
                st.warning("Por favor, primero sube un archivo CSV.")
            else:
                try:
                    df_importado = pd.read_csv(archivo)
                except Exception:
                    st.error("El archivo cargado no es un CSV válido.")
                    return

                columnas_esperadas = ["descripcion", "monto", "fecha", "categoria", "tipo"]
                columnas_faltantes = [col for col in columnas_esperadas if col not in df_importado.columns]

                if columnas_faltantes:
                    st.error(
                        f"No se pudo importar. El CSV debe contener las siguientes columnas esperadas: {', '.join(columnas_esperadas)}")
                    return

                contador = 0
                try:
                    for _, fila in df_importado.iterrows():
                        nueva_tx = Transaccion(
                            descripcion=fila["descripcion"],
                            monto=fila["monto"],
                            fecha=date.fromisoformat(str(fila["fecha"]).strip()),
                            categoria=fila["categoria"],
                            tipo=fila["tipo"]
                        )
                        st.session_state.cartera.agregar_transaccion(nueva_tx)
                        contador += 1

                    st.success(f"¡Éxito! Se importaron {contador} transacciones correctamente.")

                except ValueError as e:
                    st.error(
                        f"Error en el formato de los datos dentro del CSV. Asegúrate de que 'monto' sea un número válido y 'fecha' tenga el formato AAAA-MM-DD. Detalles: {e}")
                except Exception as e:
                    st.error(f"Ocurrió un error inesperado al procesar las filas: {e}")


def mostrar_transacciones(lista_filtrada):
    st.subheader("transacciones registradas: ")
    if lista_filtrada:
        df = pd.DataFrame([t.a_diccionario() for t in lista_filtrada])
        st.dataframe(df)

        csv_en_memoria = df.to_csv(index=False).encode('utf-8')
        st.download_button(
            label="Descargar transacciones",
            data=csv_en_memoria,
            file_name="mis_transacciones.csv",
            mime="text/csv"
        )
    else:
        st.info("No hay transacciones registradas")


def mostrar_resumen(lista_filtrada):
    if not lista_filtrada:
        st.info("No hay transacciones registradas para calcular el resumen")
        return

    cartera = st.session_state.cartera
    ingresos = cartera.obtener_ingresos(lista_filtrada)
    gastos = cartera.obtener_gastos(lista_filtrada)
    balance = cartera.obtener_balance(lista_filtrada)
    gasto_promedio = cartera.obtener_gasto_promedio(lista_filtrada)

    col1, col2, col3, col4 = st.columns(4)
    col1.metric("Ingresos", f"${ingresos:.2f}")
    col2.metric("Gastos", f"${gastos:.2f}")
    col3.metric("Balance", f"${balance:.2f}")
    col4.metric("Gasto Promedio", f"${gasto_promedio:.2f}")


def mostrar_analisis(lista_filtrada):
    if not lista_filtrada:
        st.info("No hay transacciones registradas para calcular los graficos")
        return

    cartera = st.session_state.cartera
    gastos_por_categoria = cartera.agrupar_gastos_por_categoria(lista_filtrada)
    gastos_por_fecha = cartera.agrupar_gastos_por_fecha(lista_filtrada)

    if not gastos_por_categoria:
        st.info("No hay transacciones de tipo Gasto registradas para calcular los graficos")
        return

    df_categoria = pd.DataFrame(list(gastos_por_categoria.items()), columns=["categoria", "monto"])
    df_fecha = pd.DataFrame(list(gastos_por_fecha.items()), columns=["fecha", "monto"])

    st.subheader("Total de Gastos por Categoría")
    st.bar_chart(df_categoria, x="categoria", y="monto")

    st.subheader("Total de Gastos por Fecha")
    st.line_chart(df_fecha, x="fecha", y="monto")


# ==========================================
# 3. FLUJO PRINCIPAL DE EJECUCIÓN
# ==========================================

inicializar_estado()

with st.sidebar:
    mostrar_formulario()
    cats_sel, desde, hasta = mostrar_filtros()
    importar_csv()


txs_filtradas = st.session_state.cartera.filtrar(cats_sel, desde, hasta)

tab_resumen, tab_movimientos, tab_analisis = st.tabs(["resumen", "movimientos", "alnalisis"])

with tab_resumen:
    mostrar_resumen(txs_filtradas)

with tab_movimientos:
    mostrar_transacciones(txs_filtradas)

with tab_analisis:
    mostrar_analisis(txs_filtradas)


st.session_state.cartera.guardar_csv(ARCHIVO_DATOS)
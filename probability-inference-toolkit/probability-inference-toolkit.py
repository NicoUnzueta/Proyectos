import math
from math import sqrt, ceil, copysign, floor
from scipy.stats import f, expon, chi2, norm, binom, t


# ==========================================
# UTILIDADES DE ENTRADA DE DATOS (CLASE ESTÁTICA)
# ==========================================
class InputUtil:
    """Clase utilitaria para el manejo de entradas del usuario."""

    @staticmethod
    def leer_float(mensaje: str) -> float:
        while True:
            try:
                valor = input(mensaje).strip().replace(",", ".")
                return float(valor)
            except ValueError:
                print("Error: Ingrese un número decimal válido.")

    @staticmethod
    def leer_int(mensaje: str) -> int:
        while True:
            try:
                return int(input(mensaje).strip())
            except ValueError:
                print("Error: Ingrese un número entero válido.")


# ==========================================
# DOMINIO: MEDIAS Y VARIANZAS
# ==========================================
class CalculadorMediasVarianzas:
    """Encapsula toda la lógica de diferencias de medias y varianzas."""

    def grados_libertad_aw(self, d1, n1, d2, n2):
        v1, v2 = d1 ** 2, d2 ** 2
        num = ((v1 / n1) + (v2 / n2)) ** 2
        den = ((1 / (n1 - 1)) * (v1 / n1) ** 2) + ((1 / (n2 - 1)) * (v2 / n2) ** 2)
        ag = floor(num / den)
        print(f"Para este caso se pueden aproximar los grados de libertad a: {ag}\n")
        return ag

    def muestras_dif_medias_desvios_iguales(self):
        alfa = InputUtil.leer_float("Ingrese la probabilidad de alfa: ")
        desvio = InputUtil.leer_float("Ingrese el desvío combinado (amasado): ")
        error = InputUtil.leer_float("Ingrese el error con el que quiere trabajar: ")

        muestras = 50
        cont = 0
        print("\nVamos a iniciar siempre con una muestra de 50\n")

        while cont != 2:
            print(f"Con muestra igual a: {muestras}")
            cont += 1
            nuevo = floor(2 * ((t.ppf(1 - alfa / 2, 2 * muestras - 2) * desvio) / error) ** 2)
            print(f"Me da un nuevo valor de: {nuevo}")

            if muestras != nuevo:
                cont = 0
            muestras = nuevo

        print(f"\nPara estas condiciones, se necesita una muestra de: {muestras}")

    def dif_varianza_muestra(self):
        r = InputUtil.leer_float("Ingrese v0/v1 (recuerde que deben ser las varianzas, no desvíos): ")
        alfa = InputUtil.leer_float("Ingrese alfa: ")
        beta = InputUtil.leer_float("Ingrese beta: ")
        aDL = InputUtil.leer_int("Ingrese la cantidad de grados de libertad inicial: ")

        print(f"Raíz de r queda: {sqrt(r)}")
        cont = 0
        print()

        while cont != 2:
            cont += 1
            num = 4 * sqrt(r) * ((t.ppf(1 - alfa, aDL) + t.ppf(1 - beta, aDL) * sqrt(r)) *
                                 (t.ppf(1 - beta, aDL) + t.ppf(1 - alfa, aDL) * sqrt(r)))
            print(f"Para GL {aDL}: t1 = {t.ppf(1 - alfa, aDL):.4f} y t2 = {t.ppf(1 - beta, aDL):.4f}")
            den = (r - 1) ** 2
            nuevo = ceil(num / den)

            if aDL != nuevo:
                cont = 0
            aDL = nuevo

        print(f"\nPara estas condiciones, se necesita una muestra de: {ceil(aDL) + 1}")

    def estimacion_mdc(self):
        print("Recordar: NO se debe rechazar que los desvíos sean iguales.\n")
        d = InputUtil.leer_float("Ingrese d (media1 - media2): ")
        sa = InputUtil.leer_float("Ingrese el desvío combinado (amasado): ")
        n1 = InputUtil.leer_int("Ingrese la primera muestra: ")
        n2 = InputUtil.leer_int("Ingrese la segunda muestra: ")
        alfa = InputUtil.leer_float("Ingrese el nivel de riesgo: ")

        e = t.ppf(1 - (alfa / 2), (n1 + n2) - 2) * sa * (sqrt((1 / n1) + (1 / n2)))
        print(f"\nEl error para este caso sería de: {e}\n")
        print(f"P({d - e} <= δ <= {d + e}) = {1 - alfa}")

    def estimacion_mddc(self):
        print("Recordar: Para este paso, los desvíos son desiguales.\n")
        d = InputUtil.leer_float("Ingrese d (media1 - media2): ")
        d1 = InputUtil.leer_float("Ingrese el primer desvío: ")
        d2 = InputUtil.leer_float("Ingrese el segundo desvío: ")
        n1 = InputUtil.leer_int("Ingrese la primera muestra: ")
        n2 = InputUtil.leer_int("Ingrese la segunda muestra: ")
        alfa = InputUtil.leer_float("Ingrese el nivel de riesgo: ")

        aaw = self.grados_libertad_aw(d1, n1, d2, n2)
        v1, v2 = d1 ** 2, d2 ** 2
        e = t.ppf(1 - (alfa / 2), aaw) * (sqrt((v1 / n1) + (v2 / n2)))

        print(f"\nEl error para este caso sería de: {e}\n")
        print(f"P({d - e} <= δ <= {d + e}) = {1 - alfa}")

    def muestras_dif_medias_desvios_desiguales(self):
        d1 = InputUtil.leer_float("Ingrese el primer desvío: ")
        d2 = InputUtil.leer_float("Ingrese el segundo desvío: ")
        n1 = InputUtil.leer_int("Ingrese la primera cantidad de muestras: ")
        n2 = InputUtil.leer_int("Ingrese la segunda cantidad de muestras: ")
        e = InputUtil.leer_float("Ingrese el error con el que quiere trabajar: ")
        alfa = InputUtil.leer_float("Ingrese el nivel de riesgo: ")

        adl = self.grados_libertad_aw(d1, n1, d2, n2)
        cont = 0
        muestra = adl + 2
        v1, v2 = d1 ** 2, d2 ** 2

        while cont != 2:
            cont += 1
            nuevo = floor(((t.ppf(1 - alfa / 2, adl) / e) ** 2) * (v1 + v2))
            if nuevo != muestra:
                cont = 0
                n1, n2 = nuevo - 2, nuevo - 2
                adl = self.grados_libertad_aw(d1, n1, d2, n2)
                muestra = nuevo

        print(f"Para este caso se necesita una muestra de: {muestra}")


# ==========================================
# DOMINIO: BERNOULLI Y BINOMIAL
# ==========================================
class CalculadorBernoulli:
    """Encapsula toda la lógica de procesos de Bernoulli y ensayos de hipótesis."""

    # --- Métodos Privados ---
    def _calcular_landa_caso_1(self, p0, p1, alfa, beta):
        R = p1 / p0
        nm1N = norm.ppf(1 - beta) + norm.ppf(1 - alfa) * (copysign(abs(R) ** (1 / 3), R))
        den = 2 * (copysign(abs(R) ** (1 / 3), R) - 1)
        return nm1N / den

    def _calcular_landa_caso_2(self, p0, p1, alfa, beta):
        R = p0 / p1
        nm1N = norm.ppf(1 - alfa) + norm.ppf(1 - beta) * (copysign(abs(R) ** (1 / 3), R))
        den = 2 * (copysign(abs(R) ** (1 / 3), R) - 1)
        return nm1N / den

    def _calcular_n_por_poisson(self, p0, p1, alfa, beta, nm1):
        gdl = ceil((2 / 9) * ((nm1 + sqrt(nm1 ** 2 + 1)) ** 2))
        n1 = ceil(chi2.ppf(alfa, gdl) / (2 * p0))
        n2 = ceil(chi2.ppf(1 - beta, gdl) / (2 * p1))
        return (n1, ceil(gdl / 2)) if n1 > n2 else (n2, ceil(gdl / 2))

    def _evaluacion(self, n, rcrit, p0, p1, alfa, beta, landa):
        if p0 > 0.01:
            print("Se debe usar la cota de Camp-Meidell/Markov")
            n0 = (0.23 * (1 - p0) ** 2) / (p0 ** 3) if p0 <= 0.05 else (0.23 * p0 ** 2) / (1 - p0) ** 3
            if n < n0:
                print(f"La aproximación va a ser con Poisson ya que n0 ({n0}) > n ({n})")
                return self._calcular_n_por_poisson(p0, p1, alfa, beta, landa)
            else:
                print(f"La aproximación va a ser con Normal ya que n0 ({n0}) < n ({n})")
                return n, rcrit
        else:
            print("Se cumple la condición para la aproximación por Poisson")
            return self._calcular_n_por_poisson(p0, p1, alfa, beta, landa)

    def _calcular_n_por_normal(self, po, a, p1, power):
        z_alpha = norm.ppf(1 - a)
        z_beta = norm.ppf(1 - power)

        num = z_alpha * sqrt(po * (1 - po)) + z_beta * sqrt(p1 * (1 - p1))
        den = p1 - po
        n = ceil((num / den) ** 2)

        r_crit_der = ceil(n * po + z_alpha * sqrt(n * po * (1 - po)))
        if binom.sf(r_crit_der, n, po) > a:
            r_crit_der += 1 if binom.sf(r_crit_der + 1, n, po) < a else -1

        r_crit_izq = ceil(n * po - z_alpha * sqrt(n * po * (1 - po)))
        if binom.sf(r_crit_izq, n, po) > a:
            r_crit_izq += 1 if binom.sf(r_crit_izq, n, po) < a else -1

        return n, r_crit_der, r_crit_izq

    # --- Métodos Públicos ---
    def caso_p_menor_p0(self):
        po = InputUtil.leer_float("Ingrese p0: ")
        a = InputUtil.leer_float("Ingrese nivel de confianza (alfa): ")
        p1 = InputUtil.leer_float("Ingrese p1 (debe ser mayor a p0): ")
        b = InputUtil.leer_float("Ingrese nivel de riesgo (beta): ")

        if po > p1:
            print("Error: p0 debe ser menor que p1 para este caso.")
            return

        n, r_crit, _ = self._calcular_n_por_normal(po, a, p1, b)

        if n * po < 10 or n * (1 - po) < 10:
            landa = self._calcular_landa_caso_1(po, p1, a, b)
            n, r_crit = self._evaluacion(n, r_crit, po, p1, a, b, landa)

        print(f"N aprox: {n} | R aprox: {r_crit}\nCalculando resultados exactos...")

        resultados = []
        nexacto = n - 50
        while nexacto <= (n + 250):
            for r in range(r_crit - 30, r_crit + 230):
                alfa = binom.sf(r - 1, nexacto, po)
                beta = binom.sf(r - 1, nexacto, p1)
                if alfa <= a and beta >= 1 - b:
                    resultados.append({'nExacto': nexacto, 'r': r, 'alfa': alfa, 'beta': beta})
            nexacto += 1

        if resultados:
            mejor = max(resultados, key=lambda x: x["alfa"])
            print(f"\nCon {mejor['nExacto']} muestras se necesitan {mejor['r']} casos para rechazar H0")
            print(f"Alfa: {mejor['alfa']:.6f} | Beta: {(1 - mejor['beta']):.6f}")
        else:
            print("No se encontraron valores que cumplan las condiciones.")

    def caso_p_mayor_p0(self):
        p0 = InputUtil.leer_float("Ingrese p0: ")
        a = InputUtil.leer_float("Ingrese nivel de confianza (alfa): ")
        p1 = InputUtil.leer_float("Ingrese p1 (debe ser menor a p0): ")
        beta = InputUtil.leer_float("Ingrese nivel de riesgo (beta): ")

        if p0 < p1:
            print("Error: p0 debe ser mayor que p1 para este caso.")
            return

        naprox, _, r_crit_aprox = self._calcular_n_por_normal(p0, a, p1, beta)
        print(f"N aprox: {naprox} | R aprox: {r_crit_aprox - 1}\nCalculando resultados exactos...")

        if naprox * p0 < 10 or naprox * (1 - p0) < 10:
            landa = self._calcular_landa_caso_2(p0, p1, a, beta)
            naprox, r_crit_aprox = self._evaluacion(naprox, r_crit_aprox, p0, p1, a, beta, landa)

        resultados = []
        nexacto = naprox - 60
        while nexacto < (naprox + 200):
            for r in range(r_crit_aprox - 30, r_crit_aprox + 200):
                alfa = binom.cdf(r, nexacto, p0)
                b = 1 - binom.cdf(r + 1, nexacto, p1)
                if alfa <= a and b <= beta:
                    resultados.append({'nExacto': nexacto, 'r': r, 'alfa': alfa, 'beta': b})
            nexacto += 1

        if resultados:
            mejor = max(resultados, key=lambda x: x["alfa"])
            print(f"\nCon {mejor['nExacto']} muestras se necesitan {mejor['r']} casos para rechazar H0")
            print(f"Alfa: {mejor['alfa']:.6f} | Beta: {mejor['beta']:.6f}")
        else:
            print("No se encontraron valores que cumplan las condiciones.")

    def estimacion_p(self):
        n = InputUtil.leer_int("Cantidad de muestras totales: ")
        r = InputUtil.leer_int("Cantidad de casos exitosos: ")
        alfa = InputUtil.leer_float("Nivel de riesgo deseado (alfa): ")

        pa = (n - r + 1) / r
        fa = f.ppf(1 - alfa / 2, 2 * n - 2 * r + 2, 2 * r)
        A = 1 / (1 + pa * fa)

        pb = (n - r) / (r + 1)
        fb = f.ppf(1 - alfa / 2, 2 * r + 2, 2 * n - 2 * r)
        B = 1 / (1 + pb * (1 / fb))

        print(f"\nP( {A:.4f} <= P <= {B:.4f} ) = {1 - alfa}")

    def probabilidades_binomial(self):
        n = InputUtil.leer_int("Ingrese la cantidad de casos: ")
        p = InputUtil.leer_float("Ingrese la probabilidad: ")

        proba_t, guardado = 0, 0

        for i in range(n):
            x = InputUtil.leer_float(f"Ingrese la muestra {i + 1}: ")
            proba_igual = binom.pmf(x, n, p)
            proba_menor = binom.cdf(x, n, p)
            proba_mayor = 1 - binom.cdf(x, n, p)

            print(f"P({x} = X): {proba_igual}")
            if i < n - 1:
                print(f"P({x} < X): {proba_menor - guardado}\n")
                proba_t += proba_menor - guardado
                guardado = proba_menor
            else:
                print(f"P({x} > X): {proba_mayor}\n")
                proba_t += proba_mayor

        print(f"Con una probabilidad total de: {proba_t}")


# ==========================================
# DOMINIO: VARIABLES ALEATORIAS Y CHI CUADRADO
# ==========================================
class CalculadorVA:
    """Encapsula lógicas de Gumbel, Pareto y Chi Cuadrado."""

    def gumbell_minimo(self):
        x = InputUtil.leer_float("Media muestral: ")
        d = InputUtil.leer_float("Desvío muestral: ")
        b = (sqrt(6) * d) / math.pi
        o = x + b * 0.5772156649
        print(f"Beta aproximada: {b:.4f} | Omega: {o:.4f}")

    def gumbell_maximo(self):
        x = InputUtil.leer_float("Media muestral: ")
        d = InputUtil.leer_float("Desvío muestral: ")
        b = (sqrt(6) * d) / math.pi
        o = x - b * 0.5772156649
        print(f"Beta aproximada: {b:.4f} | Omega: {o:.4f}")

    def aprox_pareto(self):
        x = InputUtil.leer_float("Media muestral: ")
        d = InputUtil.leer_float("Desvío muestral: ")

        bc = -2
        c = -((x ** 2) / (d ** 2))
        disc = bc ** 2 - 4 * c

        if disc < 0:
            print("La ecuación no tiene raíces reales.")
            return

        b = max((-bc - sqrt(disc)) / 2, (-bc + sqrt(disc)) / 2)
        o = x * (b - 1) / b
        print(f"Beta: {b:.4f} | Omega: {o:.4f}")

    def chi_cuadrado(self):
        i = InputUtil.leer_int("Cantidad de muestras a evaluar: ")
        print("Aviso: Muestras promedio > 5 y Total de muestras > 60.")

        chi_obs = 0
        for _ in range(i):
            original = InputUtil.leer_float("Muestra inicial (observada): ")
            promedio = InputUtil.leer_float("Muestra promedio (esperada): ")

            while promedio <= 5:
                print("Error: La muestra promedio debe ser > 5.")
                promedio = InputUtil.leer_float("Ingrese nuevamente: ")

            chi_obs += ((original - promedio) ** 2) / promedio
            print(f"Chi Cuadrado Observado Acumulado = {chi_obs:.4f}")


# ==========================================
# APLICACIÓN PRINCIPAL (CONTROLADOR/UI)
# ==========================================
class AplicacionEstadistica:
    """Clase principal que maneja los menús y la interacción con el usuario."""

    def __init__(self):
        self.calc_medias = CalculadorMediasVarianzas()
        self.calc_bernoulli = CalculadorBernoulli()
        self.calc_va = CalculadorVA()

    def menu_medias_varianzas(self):
        while True:
            print("\n--- Desvíos y Medias ---")
            print("1 - Muestras para diferencia de varianza")
            print("2 - Estimación de media (desvíos iguales)")
            print("3 - Muestras para diferencia de medias (desvíos iguales)")
            print("4 - Estimación de media (desvíos desiguales)")
            print("5 - Muestras para medias (desvíos desiguales)")
            print("0 - Volver")

            opcion = InputUtil.leer_int("Opción: ")
            if opcion == 1:
                self.calc_medias.dif_varianza_muestra()
            elif opcion == 2:
                self.calc_medias.estimacion_mdc()
            elif opcion == 3:
                self.calc_medias.muestras_dif_medias_desvios_iguales()
            elif opcion == 4:
                self.calc_medias.estimacion_mddc()
            elif opcion == 5:
                self.calc_medias.muestras_dif_medias_desvios_desiguales()
            else:
                break

    def menu_bernoulli(self):
        while True:
            print("\n--- Procesos de Bernoulli ---")
            print("1 - Ensayo H0: p <= p0")
            print("2 - Ensayo H0: p >= p0")
            print("3 - Estimación de probabilidad")
            print("4 - Aproximaciones binomiales")
            print("0 - Volver")

            opcion = InputUtil.leer_int("Opción: ")
            if opcion == 1:
                self.calc_bernoulli.caso_p_menor_p0()
            elif opcion == 2:
                self.calc_bernoulli.caso_p_mayor_p0()
            elif opcion == 3:
                self.calc_bernoulli.estimacion_p()
            elif opcion == 4:
                self.calc_bernoulli.probabilidades_binomial()
            else:
                break

    def menu_va_dinamicos(self):
        while True:
            print("\n--- Modelos VA Dinámicos ---")
            print("1 - Gumbel del mínimo")
            print("2 - Gumbel del máximo")
            print("3 - Aproximaciones de Pareto")
            print("0 - Volver")

            opcion = InputUtil.leer_int("Opción: ")
            if opcion == 1:
                self.calc_va.gumbell_minimo()
            elif opcion == 2:
                self.calc_va.gumbell_maximo()
            elif opcion == 3:
                self.calc_va.aprox_pareto()
            else:
                break

    def ejecutar(self):
        while True:
            print("\n" + "=" * 40)
            print("  SISTEMA DE GESTIÓN ESTADÍSTICA (OOP)  ")
            print("=" * 40)
            print("1 - Procesos de Bernoulli")
            print("2 - Diferencias de desvíos y medias")
            print("3 - Modelos de variables aleatorias dinámicas")
            print("4 - Contrastes Chi Cuadrado")
            print("0 - Salir")

            opcion = InputUtil.leer_int("Página: ")
            if opcion == 1:
                self.menu_bernoulli()
            elif opcion == 2:
                self.menu_medias_varianzas()
            elif opcion == 3:
                self.menu_va_dinamicos()
            elif opcion == 4:
                self.calc_va.chi_cuadrado()
            else:
                print("Cerrando el sistema... ¡Hasta luego!")
                break


# ==========================================
# PUNTO DE ENTRADA DEL PROGRAMA
# ==========================================
if __name__ == "__main__":
    app = AplicacionEstadistica()
    app.ejecutar()
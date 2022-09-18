from math import sqrt
from utils import parser


def DCM_plot():

    file_path_fmt = "/simulation_results/{it}/Dynamic.txt"
    time_path_fmt = "/simulation_results/{it}/CollisionsTime.txt"

    events = []
    times = []

    for i in range(0, 2):
        file_path = file_path_fmt.format(it=i)
        time_path = time_path_fmt.format(it=i)
        events.append(parser.getEvents(file_path))
        times.append(_get_times(time_path))

    z = []

    step = 0.1
    clock_dfs = []

    for dfs in events:
        current_time = 0
        clock_df = []
        for df in dfs:
            if current_time <= df.time:
                clock_df.append(df)
                current_time += step
        clock_dfs.append(clock_df)



    for i in range(0, len(events)):
        z.append(_get_z(events[i]))



def _get_times(file_path: str):
    collisions_time = open(file_path)
    lines = collisions_time.readlines()
    total_time = 0
    collisions = []
    times = []

    for line in lines:
        total_time += float(line)
        collisions.append(float(line))
    times.append(collisions[0])

    for i in range(1, len(collisions)):
        times.append(times[i - 1] + collisions[i])

    times.pop(-1)

    return times


def _get_z(events: list):
    big_particle_positions_x = []
    big_particle_positions_y = []
    z_2 = []

    for event in events:
        big_particle_positions_x.append(event[0][1])
        big_particle_positions_y.append(event[0][2])

    for i in range(0, len(big_particle_positions_x)):
        z_2.append(pow(sqrt(
            pow(float(big_particle_positions_x[0]) - float(big_particle_positions_x[i]), 2) +
            pow(float(big_particle_positions_y[0]) - float(big_particle_positions_y[i]), 2
                )), 2))

    return z_2


if __name__ == "__main__":
    DCM_plot()

# EJ 4:

# ANOTAR SI SE USA FLOR O CEIL O QUE
# Elegir la curva que menor error cuadratico tenga del desplazamiento cuadratico medio y mostrar todas las rectas que elgimos Y de esta forma no hacer una regresion lineal (no recomendado por la catedra) y ademas poner las barras de errores
# para el grafico 10 elevado no e elevado
# 10 Simulaciones como minimo
# Usar 4 para usar el DCM

# EJ 3:

# Cortar todos en el mismo tiempo t (primero que se coliciona en la pared)
# cambiar el ancho del bin y graficar las curvas junto con la distribucion inicial
# Solo guardar cada 10 eventos

# PPT:
# formula del promedio no
# quitar grilla y fondo en blanco en graficos

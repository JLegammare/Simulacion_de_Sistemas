from copy import copy
from math import sqrt
import numpy as np
import plotly.graph_objects as go
from TP3.TP3_python.utils import parser

def DCM_plot():

    file_path_fmt = "/simulation_results/{it}/Dynamic.txt"
    time_path_fmt = "/simulation_results/{it}/CollisionsTime.txt"

    events = []
    times = []

    for i in range(0, 3):
        file_path = file_path_fmt.format(it=i)
        time_path = time_path_fmt.format(it=i)
        events.append(parser.getEvents(file_path))
        times.append(_get_times(time_path))

    step = 0.1
    clock_dfs = []

    for j in range(0,len(events)):
        current_time = 0
        clock_df = []
        for i in range(0,len(events[j])):
            if current_time <= times[j][i]:
                clock_df.append(events[j][i])
                current_time += step
        clock_dfs.append(copy(clock_df))

    # me quedo con la simulacion de menor tiempo
    min_time = len(times[0])
    min_time_idx = 0
    for i in range(1, len(times)):
        new_min_time = len(times[i])
        if new_min_time < min_time:
            min_time = new_min_time
            min_time_idx = i

    len_min = len(clock_dfs[0])
    clock_dfs_index = 0
    for i in range(1, len(clock_dfs)):
        new_len = len(clock_dfs[i])
        if(new_len < len_min):
            len_min = new_len
            clock_dfs_index = i


    dcs = []
    for i in range(0, len(clock_dfs)):
        dcs.append(_get_z(clock_dfs[i]))


    DCM = np.mean(list(map(lambda l: l[0:len_min],dcs)),axis=0)
    x_values = np.arange(0, min_time, step)
    error = np.std(list(map(lambda l: l[0:len_min],dcs)), axis=0)
    start_time = 2
    start_index = int(start_time / step)

    fig = go.Figure(
        data=[
            go.Scatter(
                x=x_values,
                y=DCM,
                mode='lines',
                showlegend=False
            ),
            go.Scatter(
                x=np.concatenate((x_values, x_values[::-1])),
                y=np.concatenate((DCM + error, (DCM - error)[::-1])),
                fill='toself',
                fillcolor='rgba(0,100,80,0.2)',
                line=dict(color='rgba(255,255,255,0)'),
                hoverinfo="skip",
                showlegend=False
            ),
        ],
        # layout=go(
        #     xaxis=dict(title=r'$\Large{\text{Tiempo (s)}}$', dtick=2, tick0=0,
        #                linecolor="#000000", ticks="outside",
        #                tickwidth=2, tickcolor='black', ticklen=10),
        #     yaxis=dict(title=r'$\Large{\text{DCM }(\text{m}^{\text{2}})}$',
        #                linecolor="#000000", ticks="outside",
        #                tickwidth=2, tickcolor='black', ticklen=10),
        #      font=dict(
        #         family="Arial",
        #         size=22,
        #     ),
        #     plot_bgcolor='white',
        # )
    )


    fig.show()



    # step = 0.00005
    # offset = 0.01
    # m_values = np.arange(initial_m - offset, initial_m + offset + step, step)




def _get_times(file_path: str):
    collisions_time = open("." + file_path)
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

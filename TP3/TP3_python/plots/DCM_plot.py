from math import sqrt
import plotly.graph_objects as go
from utils import parser
import plotly.express as px


def DCM_plot(events: list):

    big_particle_positions_x = []
    big_particle_positions_y = []
    z_2 = []
    for event in events:
        big_particle_positions_x.append(event[0][1])
        big_particle_positions_y.append(event[0][2])

    collisions_time = open('./simulation_results/CollisionsTime.txt')
    lines = collisions_time.readlines()
    total_time = 0
    collisions = []
    times = []

    for line in lines:
        total_time += float(line)
        collisions.append(float(line))
    times.append(collisions[0])


    for i in range(1,len(collisions)):
        times.append(times[i-1]+collisions[i])
    times.pop(0)
    times.pop(-1)

    for i in range(1,len(big_particle_positions_x)):
        z_2.append(pow(sqrt(
            pow(float(big_particle_positions_x[i])-float(big_particle_positions_x[i-1]),2)+
            pow(float(big_particle_positions_y[i])-float(big_particle_positions_y[i-1]),2
                )),2))

    fig = px.scatter(x=times,y=z_2)

    fig.show()



if __name__ == "__main__":
    file_path = "/simulation_results/Dynamic.txt"
    events = parser.getEvents(file_path)
    DCM_plot(events)

# EJ 4:

# ANOTAR SI SE USA FLOR O CEIL O QUE
# Elegir la curva que menor error cuadratico tenga del desplazamiento cuadratico medio y mostrar todas las rectas que elgimos Y de esta forma no hacer una regresion lineal (no recomendado por la catedra) y ademas poner las barras de errores
# para el grafico 10 elevado no e elevado
# quitar grilla y fondo en blanco en graficos
# 10 Simulaciones como minimo
#formula del promedio no

# Para el 3 cortar todos en el mismo tiempo t (primero que se coliciona en la pared)
# cambiar el ancho del bin y graficar las curvas junto con la distribucion inicial
# Solo guardar cada 10 eventos
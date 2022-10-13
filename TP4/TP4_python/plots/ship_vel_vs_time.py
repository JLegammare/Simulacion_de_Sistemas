import plotly.graph_objects as go
import math
import numpy as np


def distance_velocity_plot(dynamic: str):
    dynamic_file = open(dynamic)
    lines = dynamic_file.readlines()

    venus_x = []
    venus_y = []
    ship_x = []
    ship_y = []

    venus_vx = []
    venus_vy = []
    ship_vx = []
    ship_vy = []

    time = []
    tot_time = 0.0
    dt = 300.0
    velocity = []
    distances = []

    for line in lines:
        values = line.split(" ")
        if values[0] == '2':
            venus_x.append(float(values[1]))
            venus_y.append(float(values[2]))
            venus_vx.append(float(values[4]))
            venus_vy.append(float(values[5]))
            #time.append(tot_time)
            #velocity.append(math.sqrt(vx ** 2 + vy ** 2))
        if values[0] == '3':
            ship_x.append(float(values[1]))
            ship_y.append(float(values[2]))
            ship_vx.append(float(values[4]))
            ship_vy.append(float(values[5]))
            # time.append(tot_time)
            # velocity.append(math.sqrt(vx**2 + vy**2))
            # tot_time += dt
        time.append(tot_time)
        tot_time += dt

    for i in range(0, len(venus_vx)):
        velocity.append(math.sqrt((ship_vx[i] - venus_vx[i])**2 + (ship_vy[i] - venus_vy[i])**2))
        distances.append(np.sqrt((ship_x[i] - venus_x[i])**2 + (ship_y[i] - venus_y[i]) ** 2))
        if time[i] >= 5765400.0:
            print(velocity[i])
            print(i)
            print(ship_x[i], venus_x[i])
            break

    min_dist = min(distances)

    min_idx = np.where(distances == min_dist)[0][0]

    rel_speed_venus = ((-ship_vx[min_idx] + venus_vx[min_idx]), (-ship_vy[min_idx] + venus_vy[min_idx]))
    print(velocity[min_idx])

    print(f'''Min distance: {min_dist} km\n'''
          f'''Time: {min_idx * 300 / (60 * 60 * 24)} days / {min_idx * 300 / (60 * 60)} hrs\n'''
          f'''Relative velocity: {rel_speed_venus} km/s - {np.linalg.norm(rel_speed_venus)}''')

    print(np.linalg.norm)


    data = [go.Scatter(
        y=velocity[:],
        x=time[:],
        line=dict(color="#e9bd15"),
        name="Modulo de la Velocidad vs Tiempo",
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo (s)', exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title='Rapidez (km/s)', exponentformat="power", showgrid=False, linecolor='black',
                       ticks='inside'),
            font=dict(
                family="Arial",
                size=30,
            ),
            plot_bgcolor='white',
        )
    )

    fig.update_layout(width=1000, height=1000)
    fig.show()


if __name__ == "__main__":
    dynamic = '../../simulation_results/Dynamic (1).txt'
    distance_velocity_plot(dynamic)

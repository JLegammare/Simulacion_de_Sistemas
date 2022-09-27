from copy import copy
from math import sqrt
import numpy as np
import plotly.graph_objects as go
from utils import parser

def DCM_plot():

    file_path_fmt = "/simulation_results/{it}/Dynamic.txt"
    time_path_fmt = "/simulation_results/{it}/CollisionsTime.txt"

    events = []
    times = []

    for i in range(0, 1):
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


    dcm_s = np.mean(list(map(lambda l: l[0:len_min],dcs)),axis=0)
    x_val = np.arange(0, min_time, step)
    error = np.std(list(map(lambda l: l[0:len_min],dcs)), axis=0)
    start_index = int(2 / step)

    fig = go.Figure(
        data=[
            go.Scatter(
                x=np.arange(0, 2, 0.1),
                y=dcm_s,
                mode='lines'
            ),
            go.Scatter(
                x=np.concatenate((x_val, x_val[::-1])),
                y=np.concatenate((dcm_s + error, (dcm_s - error)[::-1])),
                fill='toself',
                opacity=0.15,
                fillcolor='red'

            )
        ],
        layout=go.Layout(
            xaxis=dict(title='Tiempo (s)', linecolor='black', ticks='inside'),
            yaxis=dict(title='DCM', linecolor='black', ticks='inside'),
            font=dict(
                family='Arial',
                size=22,
            ),
            plot_bgcolor='white',

        )
    )


    fig.show()







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


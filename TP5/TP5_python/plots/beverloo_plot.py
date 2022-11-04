import math

import numpy as np
import plotly.graph_objects as go
from scipy.stats import linregress
from tensorflow._api.v2 import math


def beverloo_plot(static: str):
    static_file = open(static)
    lines = static_file.readlines()[1:]
    radius_avg = 0
    for line in lines:
        values = line.split(" ")
        radius_avg += float(values[1])
    radius_avg = radius_avg / 200
    print(radius_avg)

    n = 192.0 / (20.0 * 33.0 * 18)
    c = 0.01
    c_values = []
    for i in np.arange(0.01, radius_avg, 0.01):
        c_values.append(i)

    initial_d = 3
    final_d = 6

    d_values = [3.0, 4.0, 5.0, 6.0]

    q_values = [0.093, 0.18, 0.35, 0.51]
    ecms = []
    q = []
    d_step_values = []
    for c in c_values:
        d = initial_d
        d_step = 0.01
        errors = []
        while d < final_d:
            q.append( n * (5 ** 0.5) * (d - c * radius_avg) ** 1.5)
            d_step_values.append(d)
            d += d_step

        for i in range(0, len(d_values)):
            qb = n * (5 ** 0.5) * (d_values[i] - c * radius_avg) ** 1.5
            errors.append(((qb - q_values[i]) ** 2))

        ecms.append(sum(errors)/len(errors))


    fig = go.Figure(
        data=go.Scatter(
            y=ecms,
            x=c_values,
            name="C",
        ),
        layout=go.Layout(
            xaxis=dict(title='c', exponentformat="power", dtick=0.1, showgrid=False, linecolor='black',
                       ticks='inside'),
            yaxis=dict(title='ECM', exponentformat="power", showgrid=False, linecolor='black',
                       ticks='inside'),
            font=dict(
                family="Arial",
                size=24,
            ),
            plot_bgcolor='white',
        )
    )

    fig.show()

    fig = go.Figure(
        data=go.Scatter(
            y=q,
            x=d_step_values,
            name="C",
        ),
        layout=go.Layout(
            xaxis=dict(title='D (cm)', exponentformat="power", dtick=0.1, showgrid=False, linecolor='black',
                       ticks='inside'),
            yaxis=dict(title='Q', exponentformat="power", showgrid=False, linecolor='black',
                       ticks='inside'),
            font=dict(
                family="Arial",
                size=24,
            ),
            plot_bgcolor='white',
        )
    )

    fig.show()


if __name__ == "__main__":
    static = './simulation_results/Static.txt'

    beverloo_plot(static)

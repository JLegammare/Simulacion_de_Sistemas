from copy import copy

import numpy as np
import plotly.graph_objects as go

def beverloo_plot(static: str):
    static_file = open(static)
    lines = static_file.readlines()[1:]
    radius_avg = 0
    for line in lines:
        values = line.split(" ")
        radius_avg += float(values[1])
    radius_avg = radius_avg / 200
    print(radius_avg)

    n = 192.0 / (20.0 * 33.0)
    B = n * (5 ** 0.5) / 17

    c_values = []
    min_c = 0
    min_sum = 10000000000000
    for i in np.arange(0.01, radius_avg, 0.01):
        c_values.append(i)

    initial_d = 3
    final_d = 6

    d_values = [3.0, 4.0, 5.0, 6.0]
    q_values = [0.096, 0.22, 0.38, 0.57]
    d_step_values = []
    d_step = 0.01
    d = initial_d

    while d <= final_d:
        d += d_step
        d_step_values.append(d)

    ecms = []
    min_q = []

    for c in c_values:
        q = []
        errors = []
        # CURVAS
        for d_val in d_step_values:
            q.append(B * (d_val - c * radius_avg) ** 1.5)

        # PUNTOS DE BEVERLOO
        for i in range(0, len(d_values)):
            qb = B * (d_values[i] - c * radius_avg) ** 1.5
            errors.append(((qb - q_values[i]) ** 2))

        ecms.append(sum(errors) / len(errors))
        if (sum(errors) / len(errors)) < min_sum:
            min_q = copy(q)
            min_sum = sum(errors) / len(errors)
            min_c = c

    print(min_c)

    fig = go.Figure(
        data=[go.Scatter(
            y=ecms,
            x=c_values,
            name="C",
        ),
            go.Scatter(
                y=[min_sum],
                x=[min_c],
            )
        ],
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

    # TODAS LAS CURVAS
    fig = go.Figure(
        data=[
            go.Scatter(
                y=q_values,
                x=d_values,
                name="Qo",
                mode="markers",
                marker=dict(size=15)
            ),
            go.Scatter(
                y=min_q,
                x=d_step_values,
                name="Beverloo",
            )
        ],
        layout=go.Layout(
            xaxis=dict(title='D (cm)', exponentformat="power", dtick=1, showgrid=False, linecolor='black',
                       ticks='inside'),
            yaxis=dict(title='Q (1/seg)', exponentformat="power", showgrid=False, linecolor='black',
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

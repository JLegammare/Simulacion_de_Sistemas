from typing import List

import plotly.graph_objects as go


def plot_dots(particles):
    x_values: List = []
    y_values: List = []

    for i in range(len(particles)):
        x_values.append(particles[i].x)
        y_values.append(particles[i].x)
        print(particles[i].x)

    fig = go.Figure(data=go.Scatter(
        x=[1, 2, 3, 4],
        y=[10, 11, 12, 13],
        mode='markers',
        marker=dict(size=40,
                    line=dict(color='black'))
    ))

    fig.show()

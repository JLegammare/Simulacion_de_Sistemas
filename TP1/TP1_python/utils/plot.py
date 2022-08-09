from typing import List

import plotly.graph_objects as go

from models.config import Config


def plot_dots(particles: List, neighbors: List, config: Config):
    x_values: List = []
    y_values: List = []
    radius: List = []
    color: List = []
    plot_size: float = config.L * config.M *2

    for i in range(len(particles)):
        x_values.append(particles[i].x)
        y_values.append(particles[i].x)
        radius.append(particles[i].radius)
        color.append(0)

    for i in range(len(neighbors)):
        x_values.append(neighbors[i].x)
        y_values.append(neighbors[i].x)
        radius.append(neighbors[i].radius)
        color.append(1)

    fig = go.Figure(data=go.Scatter(
        x=x_values,
        y=y_values,
        mode='markers',
        marker=dict(size=radius, color=color))
    )
    fig.update_layout(
        width=plot_size,
        height=plot_size
    )

    fig.show()

from typing import List

import plotly.graph_objects as go

from models.config import Config


def plot_dots(particles: List, neighbors: List, config: Config):
    x_values: List = []
    y_values: List = []
    radius: List = []
    color: List = []
    n = len(particles)
    plot_size: float = config.L * config.M * 2

    for i in range(len(particles)):
        x_values.append(particles[i].x)
        y_values.append(particles[i].y)
        radius.append(particles[i].radius * 30)
        color.append(7)

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

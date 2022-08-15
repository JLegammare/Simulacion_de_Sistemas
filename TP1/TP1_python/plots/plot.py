import random
from typing import List

import plotly.graph_objects as go

from models.config import Config

from models.particle import Particle

PLOT_SIZE = 1000


def plot_dots(particles: List[Particle], neighbors: List[List[int]], config: Config):
    x_values = list(map(lambda p: p.x, particles))
    y_values = list(map(lambda p: p.y, particles))
    text = list(map(lambda p: "id: {id}".format(id=p.id), particles))

    fig = go.Figure()
    fig.update_layout(
        width=PLOT_SIZE,
        height=PLOT_SIZE
    )

    neighborhood_ids = random.choice(neighbors)
    neighbors_particles = []
    n = len(neighborhood_ids)

    for i in range(n):
        neighbors_particles.append(particles[neighborhood_ids[i]])

    for i in range(n):
        particles.remove(neighbors_particles[i])

    _create_circles(neighbors_particles, "LightSeaGreen", fig, config.Rc)

    chosen_particle = neighbors_particles.pop(0)

    _create_circles(neighbors_particles, "blue", fig, 0)
    _create_circles([chosen_particle], "red", fig, 0)
    _create_circles(particles, "yellow", fig, 0)

    fig.add_trace(go.Scatter(
        x=x_values,
        y=y_values,
        mode='markers',
        text=text,
    ))

    fig.show()


def _create_circles(particles: List[Particle], color, fig, rc):
    n = len(particles)

    for i in range(n):
        chosen_particle = particles[i]
        x, y = chosen_particle.x, chosen_particle.y
        radius = chosen_particle.radius
        x0, y0 = x - radius - rc, y - radius - rc
        x1, y1 = x + radius + rc, y + radius + rc
        if rc != 0:
            fig.add_shape(type="circle",
                          xref="x", yref="y",
                          x0=x0, y0=y0, x1=x1, y1=y1,
                          line_color=color,
                          )
        else:
            fig.add_shape(type="circle",
                          xref="x", yref="y",
                          x0=x0, y0=y0, x1=x1, y1=y1,
                          fillcolor=color,
                          )

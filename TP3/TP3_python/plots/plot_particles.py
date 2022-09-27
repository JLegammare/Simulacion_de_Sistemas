import random
from typing import List

import plotly.graph_objects as go

from ar.edu.itba.simulacion.models.config import Config
from ar.edu.itba.simulacion.models.particle import Particle
from ar.edu.itba.simulacion.models.utils.parser import parse_arguments, parse_particles

PLOT_SIZE = 1000


def main(config: Config):

    particles = parse_particles(config)

    x_values = list(map(lambda p: p.x, particles))
    y_values = list(map(lambda p: p.y, particles))

    text = list(map(lambda p: "id: {id}".format(id=p.id), particles))

    fig = go.Figure()
    fig.update_layout(
        width=PLOT_SIZE,
        height=PLOT_SIZE
    )

    _create_circles(particles, "yellow", fig)

    fig.add_trace(go.Scatter(
        x=x_values,
        y=y_values,
        mode='markers',
        text=text,
    ))

    fig.show()


def _create_circles(particles: List[Particle], color, fig):
    n = len(particles)

    for i in range(n):
        chosen_particle = particles[i]
        x, y = chosen_particle.x, chosen_particle.y
        radius = chosen_particle.radius
        x0, y0 = x - radius, y - radius
        x1, y1 = x + radius, y + radius

        fig.add_shape(type="circle",
                          xref="x", yref="y",
                          x0=x0, y0=y0, x1=x1, y1=y1,
                          fillcolor=color,
                          )


if __name__ == '__main__':
    conf = parse_arguments()
    main(conf)
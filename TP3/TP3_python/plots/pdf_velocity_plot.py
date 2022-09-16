from copy import copy

import plotly.graph_objects as go

from utils import parser


def pdf_velocity_plot():

    file_path = "/simulation_results/Dynamic.txt"
    events = parser.getEvents(file_path)
    velocities = _getVelocities(events)
    initial_velocities = velocities[0]
    third_q = int(len(velocities) / 3)

    last_third_particles_velocities= velocities[third_q:]
    last_third_velocities = []

    for event in last_third_particles_velocities:
        for v in event:
            last_third_velocities.append(v)

    fig = go.Figure(
        data=go.Histogram(
            x=initial_velocities,
            histnorm="probability density",
        ),
        layout=go.Layout(
            xaxis=dict(title='Velocidad (m/s)'),
            yaxis=dict(title='Función de densidad de probabilidad'),
            font=dict(
                family="Arial",
                size=23,
            )
        )
    )

    fig.update_layout(width=1000, height=1000)

    fig.show()

    fig = go.Figure(
        data=go.Histogram(
            x=last_third_velocities,
            histnorm="probability density",
        ),
        layout=go.Layout(
            xaxis=dict(title='Rapidez (m/s)'),
            yaxis=dict(title='Función de densidad de probabilidad'),
            font=dict(
                family="Arial",
                size=23,
            )
        )

    )

    fig.update_layout(width=1000, height=1000)
    fig.show()

def _getVelocities(events: list):
    velocities = []
    for event in events:
        velocities.append(copy(list(map(lambda p: float(p[4]),event))))

    return velocities




if __name__ == "__main__":
    pdf_velocity_plot()

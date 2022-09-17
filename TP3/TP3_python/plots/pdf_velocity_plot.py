from copy import copy
import numpy as np

import plotly.graph_objects as go

from TP3.TP3_python.utils import parser


def pdf_velocity_plot():
    file_path = '../../../simulation_results/110/Dynamic.txt'
    events = parser.get_events_small_particles(file_path)
    velocities = _getVelocities(events)
    initial_velocities = velocities[0]
    third_q = int(len(velocities) / 3)
    data = []

    last_third_particles_velocities = velocities[third_q:]
    last_third_velocities = []

    for event in last_third_particles_velocities:
        for v in event:
            last_third_velocities.append(v)

    hist, bin_edges = np.histogram(initial_velocities, bins=10, density=True)
    bin_centres = (bin_edges[:-1] + bin_edges[1:]) / 2.

    hist2, bin_edges2 = np.histogram(last_third_velocities, bins=40, density=True)
    bin_centres2 = (bin_edges2[:-1] + bin_edges2[1:]) / 2.

    data.append(
        go.Scatter(
            x=bin_centres,
            y=hist,
            mode="lines+markers",
            line=dict(color="Green"),
            name="Valores iniciales"
        )
    )

    data.append(
        go.Scatter(
            x=bin_centres2,
            y=hist2,
            mode="lines+markers",
            line=dict(color="Blue"),
            name="Valores último tercio"
        )
    )

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Rapidez (m/s)', linecolor='black', ticks='inside'),
            yaxis=dict(title='Densidad de probabilidad', linecolor='black', ticks='inside'),
            font=dict(
                family="Arial",
                size=23,
            ),
            plot_bgcolor='white',
        )
    )

    fig.update_layout(width=1000, height=1000)
    fig.show()


def _getVelocities(events: list):
    velocities = []
    for event in events:
        velocities.append(copy(list(map(lambda p: float(p[4]), event))))

    return velocities


if __name__ == "__main__":
    pdf_velocity_plot()

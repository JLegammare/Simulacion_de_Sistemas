import numpy as np
import plotly.graph_objects as go


def big_particle_trajectory(big_particle_position_path: str):
    big_particle_position_file = open(big_particle_position_path)
    lines = big_particle_position_file.readlines()
    x_pos = []
    y_pos = []
    time = []

    for line in lines:
        values = line.split(" ")
        time.append(float(values[0]))
        x_pos.append(float(values[1]))
        y_pos.append(float(values[2]))

    print(x_pos)
    print(y_pos)

    fig = go.Figure(
        data=go.Scatter(
            x=x_pos[:],
            y=y_pos[:],
        ),
        layout=go.Layout(
            xaxis=dict(title='X', range=[0, 6]),
            yaxis=dict(title='Y', range=[0, 6]),
            font=dict(
                family="Arial",
                size=22,
            )
        )
    )

    fig.update_layout(width=1000, height=1000)

    fig.show()


if __name__ == "__main__":
    big_particle_position_path = '../../../simulation_results/BigParticlePosition.txt'
    big_particle_trajectory(big_particle_position_path)

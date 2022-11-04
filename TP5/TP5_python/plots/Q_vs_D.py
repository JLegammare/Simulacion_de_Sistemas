import numpy as np
import plotly.graph_objects as go
from scipy.stats import linregress


def Q_D_plot(particles_times: list[str]):
    stderr = []
    slope = []

    d_values = [3, 4, 5, 6]

    for file in particles_times:

        times = []
        particles_times_file = open(file)
        lines = particles_times_file.readlines()
        for line in lines:
            times.append(float(line))

        np_times = np.array(times)
        np_particles = np.array([*range(1, len(np_times) + 1, 1)])
        rg = linregress(x=np_times, y=np_particles)
        ecm = (sum((np_particles - np_times * rg.slope + rg.intercept) ** 2)) / len(np_times)
        print(ecm)

        stderr.append(rg.stderr)
        slope.append(rg.slope)

    fig = go.Figure(
        data=go.Scatter(
            x=d_values,
            y=slope,
            error_y=dict(type='data', array=stderr),
            mode='lines',
            showlegend=False
        ),
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

    fig.update_layout(width=1000, height=1000)
    fig.show()


if __name__ == "__main__":
    particle_times = [
        './simulation_results/3/Times.txt',
        './simulation_results/4/Times4.txt',
        './simulation_results/5/Times5.txt',
        './simulation_results/6/Times6.txt'
    ]

    Q_D_plot(particle_times)

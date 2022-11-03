import numpy as np
import plotly.graph_objects as go
from scipy.stats import linregress


def ECM_plot(particles_times: list[str]):
    stderr = []
    slope = []

    omega_values = [5.0, 10.0, 15.0, 20.0, 30.0, 50.0]

    for file in particles_times:

        times = []
        particles_times_file = open(file)
        lines = particles_times_file.readlines()
        for line in lines:
            times.append(float(line))

        np_times = np.array(times)
        np_particles = np.array([*range(0, len(np_times), 1)])
        rg = linregress(x=np_times, y=np_particles)
        ecm = (sum((np_particles - np_times * rg.slope + rg.intercept)) ** 2) / len(np_times)
        print(ecm)
        lrg_y_values = np_times * rg.slope + rg.intercept
        fig = go.Figure(
            data=[
                go.Scatter(
                    x=np_times,
                    y=np_particles,
                    showlegend=False
                ),
                go.Scatter(
                    x=times,
                    y=lrg_y_values,
                    showlegend=False
                )
            ],
            layout=go.Layout(
                xaxis=dict(title='Tiempo (s)', exponentformat="power", showgrid=False, linecolor='black',
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
        stderr.append(rg.stderr)
        slope.append(rg.slope)

    fig = go.Figure(
        data=go.Scatter(
            x=omega_values,
            y=slope,
            error_y=dict(type='data', array=stderr),
            mode='lines',
            showlegend=False
        ),
        layout=go.Layout(
            xaxis=dict(title='w(Hz)', exponentformat="power", showgrid=False, linecolor='black',
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
    particle_times = [
        './simulation_results/5.000000/Times.txt',
        './simulation_results/10.000000/Times.txt',
        './simulation_results/15.000000/Times.txt',
        './simulation_results/20.000000/Times.txt',
        './simulation_results/30.000000/Times.txt',
        './simulation_results/50.000000/Times.txt'
    ]

    ECM_plot(particle_times)

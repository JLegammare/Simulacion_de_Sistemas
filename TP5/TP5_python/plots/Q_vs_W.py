import numpy as np
import plotly.graph_objects as go
from scipy.stats import linregress


def Q_W_plot(particles_times: list[str]):
    stderr = []
    q_promedios = []
    slopes =[]

    delta_N = 10

    omega_values = [5.0, 10.0, 15.0, 20.0, 30.0, 50.0]

    for file in particles_times:

        times = []
        particles_times_file = open(file)
        lines = particles_times_file.readlines()
        for line in lines:
            times.append(float(line))

        np_times = np.array(times)
        np_particles = np.array([*range(1, len(np_times)+1, 1)])
        rg = linregress(x=np_times, y=np_particles)
        ecm = (sum((np_particles - np_times * rg.slope + rg.intercept) ** 2)) / len(np_times)
        lrg_y_values = np_times * rg.slope + rg.intercept
        slopes.append(rg.slope)
        q_s = []
        t_medios = []

        for i in range(0,len(np_times)-delta_N):
            t0= np_times[0+i]
            tn= np_times[delta_N+i]
            q = delta_N / (tn-t0)
            q_s.append(q)
            t_medios.append(t0)

        q_final = sum(q_s)/len(q_s)

        q_promedios.append(q_final)

        # fig = go.Figure(
        #     data=[
        #         go.Scatter(
        #             x=np_times,
        #             y=np_particles,
        #             showlegend=False
        #         ),
        #         go.Scatter(
        #             x=times,
        #             y=lrg_y_values,
        #             showlegend=False
        #         )
        #     ],
        #     layout=go.Layout(
        #         title=file,
        #         xaxis=dict(title='Tiempo (s)', exponentformat="power", showgrid=False, linecolor='black',
        #                    ticks='inside'),
        #         yaxis=dict(title='ECM', exponentformat="power", showgrid=False, linecolor='black',
        #                    ticks='inside'),
        #         font=dict(
        #             family="Arial",
        #             size=24,
        #         ),
        #         plot_bgcolor='white',
        #     )
        # )

        fig = go.Figure(
            data=[
                go.Scatter(
                    x=t_medios,
                    y=q_s,
                    showlegend=False
                ),
            ],
            layout=go.Layout(
                title=file,
                xaxis=dict(title='Tiempo medio (s)', exponentformat="power", showgrid=False, linecolor='black',
                           ticks='inside'),
                yaxis=dict(title='Q', exponentformat="power", showgrid=False, linecolor='black',
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
        stderr.append(rg.stderr)
        q_s.append(rg.slope)

    fig = go.Figure(
        data=[
            go.Scatter(
                x=omega_values,
                y=q_promedios,
                error_y=dict(type='data', array=stderr),
                mode='lines',
                showlegend=False
            ),
            go.Scatter(
                x=omega_values,
                y=slopes,
                error_y=dict(type='data', array=stderr),
                mode='lines',
                showlegend=False
            ),
        ],
        layout=go.Layout(
            xaxis=dict(title='w (Hz)', exponentformat="power", dtick=5, showgrid=False, linecolor='black',
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
        './simulation_results/5.000000/Times.txt',
        './simulation_results/10.000000/Times.txt',
        './simulation_results/15.000000/Times.txt',
        './simulation_results/20.000000/Times.txt',
        './simulation_results/30.000000/Times.txt',
        './simulation_results/50.000000/Times.txt'
    ]

    Q_W_plot(particle_times)

import plotly.graph_objects as go


def particles_vs_time_plot(particles_times):
    data = []
    j: int = 0
    for p in particles_times:
        particles_times_file = open(p)
        lines = particles_times_file.readlines()
        time = []
        particles = []
        i: int = 1

        w = [5, 10, 15, 20, 30, 50]
        for line in lines:
            time.append(float(line))
            particles.append(i)
            i += 1

        data.append(go.Scatter(
            y=particles[:],
            x=time[:],
            marker=dict(size=10),
            name=" w = " + str(w[j]) + " Hz",
        ))
        j += 1

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo (s)', exponentformat="power", showgrid=False, linecolor='black',
                       ticks='inside'),
            yaxis=dict(title='Cantidad Partículas', exponentformat="power", showgrid=False, linecolor='black',
                       ticks='inside'),
            font=dict(
                family="Arial",
                size=40,
            ),
            plot_bgcolor='white',
        )
    )

    fig.update_layout(width=2000, height=1000)
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

    particles_vs_time_plot(particle_times)

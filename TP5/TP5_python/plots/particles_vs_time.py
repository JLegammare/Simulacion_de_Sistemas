import plotly.graph_objects as go


def particles_vs_time_plot(particles_time: str):
    particles_time_file = open(particles_time)
    lines = particles_time_file.readlines()
    time = []
    particles = []
    i:int = 0
    for line in lines:
        time.append(float(line))
        particles.append(i)
        i += 1

    data = [go.Scatter(
        y=particles[:],
        x=time[:],
        line=dict(color="#e9bd15"),
        name="Cantidad de Partículas vs Tiempo",
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo (s)', exponentformat="power", showgrid=False, linecolor='black',
                       ticks='inside'),
            yaxis=dict(title='Cantidad Partículas', exponentformat="power", showgrid=False, linecolor='black',
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
    particles_time = './simulation_results/Time.txt'
    particles_vs_time_plot(particles_time)

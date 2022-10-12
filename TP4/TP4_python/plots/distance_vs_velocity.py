import plotly.graph_objects as go


def distance_velocity_plot(distance_vel: str):
    distance_vel_file = open(distance_vel)
    lines = distance_vel_file.readlines()
    velocity = []
    min_distance = []
    for line in lines:
        values = line.split(",")
        print(values[2])
        print(values[2])
        velocity.append(float(values[1]))
        min_distance.append(float(values[2]))

    data = [go.Scatter(
        y=min_distance[:],
        x=velocity[:],
        line=dict(color="yellow"),
        name="Distancia mínima vs Rapidez",
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Rapidez (km/s)', exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title='Distancia (km)', exponentformat="power", showgrid=False, linecolor='black',
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
    distance_vel = './simulation_results/Venus_Mission/VenusTrip/Velocity/SpeedDistance.txt'
    distance_velocity_plot(distance_vel)

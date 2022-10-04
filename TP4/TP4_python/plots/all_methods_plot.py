import plotly.graph_objects as go


def particle_trajectory(beeman_position: str, verlet_position: str, gear_position: str, analytic_position: str):
    beeman_position_file = open(beeman_position)
    lines1 = beeman_position_file.readlines()[1:]
    time1 = []
    x_pos1 = []

    verlet_position_file = open(verlet_position)
    lines2 = verlet_position_file.readlines()[1:]
    time2 = []
    x_pos2 = []

    gear_position_file = open(gear_position)
    lines3 = gear_position_file.readlines()[1:]
    time3 = []
    x_pos3 = []

    analytic_file = open(analytic_position)
    lines4 = analytic_file.readlines()[1:]
    time4 = []
    x_pos4 = []

    for line in lines1:
        values = line.split(" ")
        time1.append(float(values[0]))
        x_pos1.append(float(values[1]))

    for line in lines2:
        values = line.split(" ")
        time2.append(float(values[0]))
        x_pos2.append(float(values[1]))

    for line in lines3:
        values = line.split(" ")
        time3.append(float(values[0]))
        x_pos3.append(float(values[1]))

    for line in lines4:
        values = line.split(" ")
        time4.append(float(values[0]))
        x_pos4.append(float(values[1]))

    data = [go.Scatter(
        y=x_pos1[:],
        x=time1[:],
        line=dict(color="Green"),
        name="Beeman",
    ), go.Scatter(
        y=x_pos2[:],
        x=time2[:],
        line=dict(color="Blue"),
        name="Verlet"
    ), go.Scatter(
        y=x_pos3[:],
        x=time3[:],
        line=dict(color="Red"),
        name="Gear"
    ), go.Scatter(
        y=x_pos4[:],
        x=time4[:],
        line=dict(color="Yellow"),
        name="Analytic"
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo (s)',  exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title='Posición (m)', dtick=0.2, exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            font=dict(
                family="Arial",
                size=30,
            ),
            plot_bgcolor='white',
        )
    )

    fig.update_layout(width=1000, height=1000)
    fig.show()



if __name__ == "__main__":
    beeman_position = '../../simulation_results/BeemanPosition.txt'
    verlet_position = '../../simulation_results/VerletPosition.txt'
    gear_position = '../../simulation_results/GearPosition.txt'
    analytic_position = '../../simulation_results/AnalyticPosition.txt'
    particle_trajectory(beeman_position, verlet_position, gear_position, analytic_position)

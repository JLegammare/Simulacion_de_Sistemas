import plotly.graph_objects as go


def errors(beeman_error: str, verlet_error: str, gear_error: str):
    beeman_error_file = open(beeman_error)
    lines1 = beeman_error_file.readlines()[1:]
    error1 = []
    dt1 = []

    verlet_error_file = open(verlet_error)
    lines2 = verlet_error_file.readlines()[1:]
    error2 = []
    dt2 = []

    gear_error_file = open(gear_error)
    lines3 = gear_error_file.readlines()[1:]
    error3 = []
    dt3 = []

    for line in lines1:
        values = line.split(" ")
        error1.append(float(values[0]))
        dt1.append(float(values[1]))

    for line in lines2:
        values = line.split(" ")
        error2.append(float(values[0]))
        dt2.append(float(values[1]))

    for line in lines3:
        values = line.split(" ")
        error3.append(float(values[0]))
        dt3.append(float(values[1]))

    data = [go.Scatter(
        y=dt1[:],
        x=error1[:],
        line=dict(color="Green"),
        name="Beeman",
        marker=dict(size=10)
    ), go.Scatter(
        y=dt2[:],
        x=error2[:],
        line=dict(color="Blue"),
        name="Verlet",
        marker=dict(size=10)
    ), go.Scatter(
        y=dt3[:],
        x=error3[:],
        line=dict(color="Red"),
        name="Gear",
        marker=dict(size=10)
    )]
    r'$\Large{\text{ECM } (\text{m}^{\text{2}})}$'
    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title=r'$\Large{\text{dt (s) }}$', exponentformat="power", type="log", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title=r'$\Large{\text{Error Cuadrático Medio } (\text{m}^{\text{2}})}$', type="log", exponentformat="power", showgrid=False,
                       linecolor='black',
                       ticks='inside'),
            font=dict(
                family="Arial",
                size=25,
            ),
            plot_bgcolor='white',
        )
    )

    fig.update_layout(width=1000, height=1000)
    fig.show()


if __name__ == "__main__":
    beeman_errors = '../../simulation_results/BEEMANError.txt'
    verlet_errors = '../../simulation_results/VERLETError.txt'
    gear_errors = '../../simulation_results/GEARError.txt'
    errors(beeman_errors, verlet_errors, gear_errors)

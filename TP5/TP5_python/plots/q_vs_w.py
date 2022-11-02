import plotly.graph_objects as go


def q_w_plot(q_w: str):
    q_w_file = open(q_w)
    lines = q_w_file.readlines()
    q = []
    w = []
    for line in lines:
        values = line.split(",")
        q.append(float(values[0]))
        w.append(float(values[1]))

    data = [go.Scatter(
        y=q[:],
        x=w[:],
        line=dict(color="#e9bd15"),
        name="Caudal",
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Frecuencia', exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title='Caudal', exponentformat="power", showgrid=False, linecolor='black',
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
    q_w = './simulation_results/Q.txt'
    q_w_plot(q_w)

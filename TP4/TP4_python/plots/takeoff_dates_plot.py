import plotly.graph_objects as go

from datetime import datetime


def takeoff_dates(min_distance: str):
    min_distance_file = open(min_distance)
    lines = min_distance_file.readlines()
    day = []
    min_distance = []
    for line in lines:
        values = line.split(",")
        day.append(
            datetime.strptime(values[0].replace("ART", "GMT"), '%a %b %d %H:%M:%S %Z %Y').strftime('%H:%M:%S'))
        min_distance.append(float(values[1]))

    data = [go.Scatter(
        y=min_distance[:],
        x=day[:],
        line=dict(color="#e9bd15"),
        name="Beeman",
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Hora de salida', exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title='Distancia mínima (km)', exponentformat="power", showgrid=False, linecolor='black',
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
    min_distance = './simulation_results/Venus_Mission/MinDistance12.txt'
    takeoff_dates(min_distance)

import plotly.graph_objects as go

from datetime import datetime

def takeoff_dates():
    daysStrings = ['2022-Sep-23 00:00:00.00','2022-Sep-24 00:00:00.00', '2022-Sep-25 00:00:00.00', '2022-Sep-26 00:00:00.00', '2022-Sep-27 00:00:00.00', '2022-Sep-28 00:00:00.00',
                   '2022-Sep-29 00:00:00.00','2022-Sep-30 00:00:00.00', '2022-Oct-01 00:00:00.00', '2022-Oct-02 00:00:00.00', '2022-Oct-03 00:00:00.00', '2022-Oct-04 00:00:00.00']
    days = []
    doubles = [0.1, 0.2, 0.45, 0.63, 1.56, 5.36, 9.3, 10.2, 10.45, 16.63, 21.56, 35.36]

    for day in daysStrings:
        days.append(datetime.strptime(day, '%Y-%b-%d %H:%M:%S.%f').strftime('%d-%m-%Y %H:%M'))

    data = [go.Scatter(
        y=doubles[:],
        x=days[:],
        line=dict(color="Green"),
        name="Beeman",
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo (s)', exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title='Posición (m)', exponentformat="power", showgrid=False, linecolor='black',
                       ticks='inside'),
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
    takeoff_dates()
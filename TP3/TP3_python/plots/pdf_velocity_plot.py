import plotly.graph_objects as go


def pdf_velocity_plot():
    file_path = "/simulation_results/Dynamic.txt"
    dynamic_file = open("." + file_path)

    lines = dynamic_file.readlines()
    events = []
    new_event = []
    for line in lines:
        if line == lines[0]:
            events.append(new_event.copy())
            new_event = []
        elif line == "a\n":
            pass
        else:
            st_p = list(filter(lambda c: len(c) > 0, line.split(" ")))
            new_event.append(float(st_p[4]))

    third_q = int(len(events) / 3)
    events.pop(0)

    initial_velocities = events[0]
    last_third_events = events[third_q:]
    last_third_velocities = []

    for event in last_third_events:
        for v in event:
            last_third_velocities.append(v)

    fig = go.Figure(
        data=go.Histogram(
            x=initial_velocities,
            histnorm="probability density",
        ),
        layout=go.Layout(
            xaxis=dict(title='Velocidad (m/s)'),
            yaxis=dict(title='Probability Density Function'),
            font=dict(
                family="Arial",
                size=23,
            )
        )
    )

    fig.update_layout(width=1000, height=1000)

    fig.show()

    fig = go.Figure(
        data=go.Histogram(
            x=last_third_velocities,
            histnorm="probability density",
        ),
        layout=go.Layout(
            xaxis=dict(title='Rapidez (m/s)'),
            yaxis=dict(title='Probability Density Function'),
            font=dict(
                family="Arial",
                size=23,
            )
        )

    )

    fig.update_layout(width=1000, height=1000)
    fig.show()


if __name__ == "__main__":
    pdf_velocity_plot()

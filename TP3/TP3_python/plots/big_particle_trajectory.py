import plotly.graph_objects as go


def big_particle_trajectory(big_particle_v1: str, big_particle_v2: str, big_particle_v3: str):
    big_particle_position_file_v1 = open(big_particle_v1)
    lines1 = big_particle_position_file_v1.readlines()
    time1 = []
    x_pos1 = []
    y_pos1 = []
    total_time1 = 0

    big_particle_position_file_v2 = open(big_particle_v2)
    lines2 = big_particle_position_file_v2.readlines()
    time2 = []
    x_pos2 = []
    y_pos2 = []
    total_time2 = 0

    big_particle_position_file_v3 = open(big_particle_v3)
    lines3 = big_particle_position_file_v3.readlines()
    time3 = []
    x_pos3 = []
    y_pos3 = []
    total_time3 = 0

    for line in lines1:
        values = line.split(" ")
        total_time1 += float(values[0])

    for line2 in lines2:
        values = line2.split(" ")
        total_time2 += float(values[0])

    for line3 in lines3:
        values = line3.split(" ")
        total_time3 += float(values[0])


    total_time_min = min(total_time1, total_time2, total_time3)

    print(total_time_min)
    print("---")
    print(total_time1)
    print(total_time2)
    print(total_time3)



    total_time = 0
    for line in lines1:
        values = line.split(" ")
        time = float(values[0])
        total_time += time
        if total_time <= total_time_min:
            time1.append(time)
            x_pos1.append(float(values[1]))
            y_pos1.append(float(values[2]))

    total_time = 0
    for line2 in lines2:
        values = line2.split(" ")
        time = float(values[0])
        total_time += time
        if total_time <= total_time_min:
            time2.append(time)
            x_pos2.append(float(values[1]))
            y_pos2.append(float(values[2]))

    total_time = 0
    for line3 in lines3:
        values = line3.split(" ")
        time = float(values[0])
        total_time += time
        if total_time <= total_time_min:
            time3.append(time)
            x_pos3.append(float(values[1]))
            y_pos3.append(float(values[2]))


    data = [go.Scatter(
        x=x_pos1[:],
        y=y_pos1[:],
        line=dict(color="Green"),
        name="v=[0, 2] m/s",
    ), go.Scatter(
        x=x_pos2[:],
        y=y_pos2[:],
        line=dict(color="Blue"),
        name="v=[2, 4] m/s"
    ), go.Scatter(
        x=x_pos3[:],
        y=y_pos3[:],
        line=dict(color="Red"),
        name="v=[4, 6] m/s"
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='X (m)', range=[0, 6], exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title='Y (m)', range=[0, 6], exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            font=dict(
                family="Arial",
                size=23,
            ),
            plot_bgcolor='white',
        )
    )

    last_x_pos1 = x_pos1[-1]
    last_y_pos1 = y_pos1[-1]
    x0 = last_x_pos1 - 0.7
    x1 = last_x_pos1 + 0.7
    y0 = last_y_pos1 - 0.7
    y1 = last_y_pos1 + 0.7

    fig.add_shape(type="circle",
                  xref="x", yref="y",
                  x0=x0, y0=y0, x1=x1, y1=y1,
                  opacity=0.2,
                  line_color="Green",
                  fillcolor="Green"
                  )

    last_x_pos2 = x_pos2[-1]
    last_y_pos2 = y_pos2[-1]
    x0 = last_x_pos2 - 0.7
    x1 = last_x_pos2 + 0.7
    y0 = last_y_pos2 - 0.7
    y1 = last_y_pos2 + 0.7

    fig.add_shape(type="circle",
                  xref="x", yref="y",
                  x0=x0, y0=y0, x1=x1, y1=y1,
                  opacity=0.2,
                  line_color="Blue",
                  fillcolor="Blue"
                  )

    last_x_pos3 = x_pos3[-1]
    last_y_pos3 = y_pos3[-1]
    x0 = last_x_pos3 - 0.7
    x1 = last_x_pos3 + 0.7
    y0 = last_y_pos3 - 0.7
    y1 = last_y_pos3 + 0.7

    fig.add_shape(type="circle",
                  xref="x", yref="y",
                  x0=x0, y0=y0, x1=x1, y1=y1,
                  opacity=0.2,
                  line_color="Red",
                  fillcolor="Red"
                  )

    fig.add_shape(
            # Rectangle with reference to the plot
            type="rect",
            xref="paper",
            yref="paper",
            x0=0,
            y0=0,
            x1=1.0,
            y1=1.0,
            line=dict(
                color="black",
                width=1,
            )
         )

    fig.update_layout(width=1000, height=1000)

    fig.show()


if __name__ == "__main__":
    big_particle_v1 = '../../../simulation_results/BigParticlePositionV1.txt'
    big_particle_v2 = '../../../simulation_results/BigParticlePositionV2.txt'
    big_particle_v3 = '../../../simulation_results/BigParticlePositionV3.txt'
    big_particle_trajectory(big_particle_v1, big_particle_v2, big_particle_v3)

#
#
# 0 0s
# 1 2s
# 2 3s
# 3 5s
# 4 6s
#
#
# 3s |
# 2s ||
# 1s |||
#
#
#
# I
# I   I
# I   I  I
# ---------------->
#
# CollisionTime.txt

# fig.add_shape(
    #    # Rectangle with reference to the plot
    #    type="rect",
    #    xref="paper",
    #    yref="paper",
    #    x0=0,
    #    y0=0,
    #    x1=1.0,
    #    y1=1.0,
    #    line=dict(
    #        color="black",
    #        width=1,
    #    )
    # )



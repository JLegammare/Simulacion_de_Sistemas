import plotly.graph_objects as go


def big_particle_trajectory(big_particle_v1: str, big_particle_v2: str, big_particle_v3: str):
    big_particle_position_file_v1 = open(big_particle_v1)
    lines1 = big_particle_position_file_v1.readlines()
    time1 = []
    x_pos1 = []
    y_pos1 = []

    big_particle_position_file_v2 = open(big_particle_v2)
    lines2 = big_particle_position_file_v2.readlines()
    time2 = []
    x_pos2 = []
    y_pos2 = []

    big_particle_position_file_v3 = open(big_particle_v3)
    lines3 = big_particle_position_file_v3.readlines()
    time3 = []
    x_pos3 = []
    y_pos3 = []

    for line in lines1:
        values = line.split(" ")
        time1.append(float(values[0]))
        x_pos1.append(float(values[1]))
        y_pos1.append(float(values[2]))

    for line2 in lines2:
        values = line2.split(" ")
        time2.append(float(values[0]))
        x_pos2.append(float(values[1]))
        y_pos2.append(float(values[2]))

    for line3 in lines3:
        values = line3.split(" ")
        time3.append(float(values[0]))
        x_pos3.append(float(values[1]))
        y_pos3.append(float(values[2]))

    data = [go.Scatter(
        x=x_pos1[:],
        y=y_pos1[:],
        name="v=[0, 2] m/s"
    ), go.Scatter(
        x=x_pos2[:],
        y=y_pos2[:],
        name="v=[2, 4] m/s"
    ), go.Scatter(
        x=x_pos3[:],
        y=y_pos3[:],
        name="v=[4, 6] m/s"
    )]

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='X', range=[0, 6]),
            yaxis=dict(title='Y', range=[0, 6]),
            font=dict(
                family="Arial",
                size=23,
            )
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



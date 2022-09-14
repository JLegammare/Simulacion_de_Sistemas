import numpy as np
import plotly.graph_objects as go

from scipy.stats import norm


def pdf_collision_frequency(collisions_time_path: str):
    collisions_time = open(collisions_time_path)
    lines = collisions_time.readlines()
    count = 0
    total_time = 0
    collisions = []
    avg_cols_times = []

    for line in lines:
        count += 1
        total_time += float(line)
        collisions.append(float(line))

    for i in range(0, len(collisions)):
        avg_cols_times.append(collisions[i]/total_time)

    avg = count / total_time
    avg_cols_time = np.mean(collisions)
    standard_deviation = np.std(collisions)
    pdf = norm.pdf(collisions, avg_cols_time, standard_deviation)

    fig = go.Figure(
        data=go.Histogram(
            x=collisions,
            y=pdf,
            histnorm="probability density",
           # nbinsx=40
        ),
        layout=go.Layout(
            title=dict(text='Collision Times'),
            xaxis=dict(title='Time (s)'),
            yaxis=dict(title='PDF'),
            font=dict(
                family="Arial",
                size=22,
            )
        )
    )

    fig.update_layout(width=1000, height=1000)
    fig.show()

    fig = go.Figure(
        data=go.Table(
            header=dict(values=['Average Frequency', 'Average Time'], height=40),
            cells=dict(values=[[avg_cols_time], [total_time/count]], height=40)
        ),
        layout=go.Layout(
            title=dict(text='Collision Times'),
            font=dict(
                family="Arial",
                size=22,
            )
        )
    )

    fig.update_layout(width=1000, height=1000)
    fig.show()


if __name__ == "__main__":
    collisions_time_path = '../../../simulation_results/CollisionsTime.txt'
    pdf_collision_frequency(collisions_time_path)

# 0.000000
# 0.000485
# 0.000641
# 0.003501
# 0.000528
# 0.000220
# 0.002116
# 0.003777
# 0.000631
# 0.003896
# 0.001515
# 0.002241
# 0.000053
# 0.001264
# 0.000881
# 0.000563
# 0.000971
# 0.001861
# 0.000593
# 0.000157
# 0.000516
# 0.001865
# 0.003094
# 0.004670
# 0.001574
# 0.000326
# 0.000223
# 0.000328
# 0.000806
# 0.006018
# 0.000455
# 0.005606
# 0.007544
# 0.002249
# 0.001818
# 0.001630
# 0.003889
# 0.001162
# 0.000123
# 0.000798
# 0.002779
# 0.000442
# 0.000411
# 0.001303
# 0.000493
# 0.002156
# 0.000186
# 0.003230
# 0.000049
# 0.002199

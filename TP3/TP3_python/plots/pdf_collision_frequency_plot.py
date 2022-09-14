import numpy as np
import plotly.graph_objects as go

def pdf_collision_frequency(collisions_time_path: str):
    collisions_time = open(collisions_time_path)
    lines = collisions_time.readlines()
    total_time = 0
    collisions = []
    avg_cols_times = []

    for line in lines:
        total_time += float(line)
        collisions.append(float(line))  #

    avg = (len(lines)-1)/ total_time
    avg_cols_time = np.mean(collisions)
    standard_deviation = np.std(collisions)


    fig = go.Figure(
        data=go.Histogram(
            x=collisions,
            histnorm="probability density",
            nbinsx=40
        ),
        layout=go.Layout(
            title=dict(text='Collision Times'),
            xaxis=dict(title='Time (s)'),
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
    collisions_time_path = './simulation_results/CollisionsTime.txt'
    pdf_collision_frequency(collisions_time_path)

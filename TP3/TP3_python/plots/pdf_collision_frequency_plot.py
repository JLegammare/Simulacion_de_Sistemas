import numpy as np
import plotly.graph_objects as go


def pdf_collision_frequency(collisions_time_path1: str, collisions_time_path2: str, collisions_time_path3: str, collisions_time_path4: str):
    paths = [collisions_time_path1, collisions_time_path2, collisions_time_path3, collisions_time_path4]
    data = []
    j = 110

    for i in paths:
        collisions_time = open(i)
        lines = collisions_time.readlines()
        total_time = 0
        collisions = []
        avg_cols_times = []

        for line in lines:
            total_time += float(line)
            collisions.append(float(line))

        avg = (len(lines) - 1) / total_time
        avg_cols_time = np.mean(collisions)
        standard_deviation = np.std(collisions)

        hist, bin_edges = np.histogram(collisions, bins=40, density=True)
        bin_centres = (bin_edges[:-1] + bin_edges[1:]) / 2.

        data.append(go.Scatter(
            x=bin_centres,
            y=hist,
            mode='lines',
            name='N=' + str(j)
        ))
        j += 10

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo (s)', exponentformat="power", showgrid=False, linecolor='black', ticks='inside'),
            yaxis=dict(title='PDF', exponentformat='power', showgrid=False, linecolor='black', ticks='inside', type="log"),
            font=dict(
                family="Arial",
                size=22,
            ),
            plot_bgcolor='white',

        )
    )

    fig.update_layout(width=1000, height=1000)
    fig.show()


if __name__ == "__main__":
    collisions_time_path1 = '../../../simulation_results/110/CollisionsTime.txt'
    collisions_time_path2 = '../../../simulation_results/120/CollisionsTime.txt'
    collisions_time_path3 = '../../../simulation_results/130/CollisionsTime.txt'
    collisions_time_path4 = '../../../simulation_results/140/CollisionsTime.txt'
    pdf_collision_frequency(collisions_time_path1, collisions_time_path2, collisions_time_path3, collisions_time_path4)

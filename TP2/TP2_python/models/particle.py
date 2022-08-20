from dataclasses import dataclass


@dataclass
class Particle:
    id: int
    x: float
    y: float
    radius: float
    speed: float
    angle: float

from dataclasses import dataclass


@dataclass
class Config:
    N: int
    M: int
    Rc: float
    L: float
    static_file: str
    dynamic_file: str
    output_file: str

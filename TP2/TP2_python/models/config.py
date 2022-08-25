from dataclasses import dataclass


@dataclass
class Config:
    static_file: str
    dynamic_file: str
    va_time_file: str
    run_times: int
    N: int
    L: float

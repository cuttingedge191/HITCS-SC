package exercise_5_4.strategy;

import java.util.List;
import java.util.Map;

import exercise_5_4.field.Lot;
import exercise_5_4.field.ParkingField;
import exercise_5_4.parkable.Parkable;


public abstract class ParkingStrategy {
	public abstract Lot selectLot(ParkingField pf, List<Lot> lots, Map<Lot, Parkable> status, Parkable c) throws Exception;
}

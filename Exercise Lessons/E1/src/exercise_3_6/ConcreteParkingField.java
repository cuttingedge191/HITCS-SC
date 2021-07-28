package exercise_3_6;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;

public class ConcreteParkingField implements ParkingField {

	// Rep
	private final List<Lot> lots = new ArrayList<>(); // һ�鳵λ
	private final Map<Lot, Parkable> status = new HashMap<>(); // ռ�����
	private final List<Record> records = new ArrayList<>(); // ͣ����¼

	// Rep invariants:
	// lots.size() >= 5;
	// lots.size() >= status.size();
	// status�е�ÿ��key����lots�г��֣�
	// status�е�values�в������ظ���
	// status�е�value����Car�Ŀ��С�ڵ�����Ӧ��key����λ���Ŀ�ȣ�
	// ��records�е�ÿ��Record�������record.getTimeOut()Ϊ�գ�
	// ��<record.getLot(),record.getCar()>�ض�������status��

	// Abstraction function:
	// ������һ��ͣ��������ͣ������lots.size()����λ
	// lots��ÿ��Ԫ��l������һ����λ�����źͿ����l.getNumber()��l.getWidth()
	// l��ͣ�ĳ���status.get(l)
	// �ó���������ͣ����¼��records�����е�Ԫ��r��ʾһ��ͣ����¼��������
	// ����r.getCar()��r.getTimeIn()ʱ��ͣ����r.getLot()��λ�ϣ���r.getTimeOut()�뿪������r.getFee()Ԫ

	private void checkRep() {

		assert lots.size() >= 5;
		assert lots.size() >= status.size();

		Set<Parkable> parkingCars = new HashSet<>();
		for (Lot lot : status.keySet()) {
			Parkable car = status.get(lot);
			assert lots.contains(lot);
			assert !parkingCars.contains(car);
			parkingCars.add(car);
			assert lot.getWidth() >= car.getWidth();
		}

		for (Record record : records) {
			if (record.getTimeOut() == null) {
				assert status.containsKey(record.getLot());
				assert status.get(record.getLot()).equals(record.getCar());
			}
		}
	}

	/**
	 * ����һ���µ�ͣ����
	 * 
	 * @param lots key�ǳ�λ��ţ���Ȼ������value�ǳ�λ��ȣ���Ȼ������lots����>=5��
	 * @return һ��ͣ�������󣬰�����lots.size()����λ������λ�ı��������lots�е�KVһ�£��Ҹ���λ�Ͼ�δ��ͣ��
	 * @throws ���lots���Ϸ�
	 */
	public ConcreteParkingField(Map<Integer, Integer> lotsInfo) throws Exception {

		// check pre-condition, fail fast
		if (lotsInfo.size() < 5)
			throw new Exception();

		for (Map.Entry<Integer, Integer> entry : lotsInfo.entrySet()) {
			int num = entry.getKey();
			int width = entry.getValue();

			// check pre-condition, fail fast
			if (width <= 0)
				throw new Exception();

			// work
			Lot lot = new Lot(num, width);
			this.lots.add(lot);
		}

		// check post-condition
		assert this.getNumberOfLots() == lotsInfo.size();
		assert this.status.size() == 0;

		// check RI
		checkRep();
	}

	public ConcreteParkingField(int[] nos, int[] widths) {
		// TODO Auto-generated method stub
		checkRep();
	}

	/**
	 * ��ĳ��ͣ��λ��ͣ�� ���ƺ�Ϊplate�ĳ�����֮ǰûͣ�ڳ�����ִ�к�ͣ���˳�λ��Ϊnum�ĳ�λ�ϣ��ó�λ��ȴ��ڳ���� ������λ��״̬����
	 * 
	 * @param plate Ҫͣ�����ĳ������ƺţ�not null
	 * @param width ���Ŀ�ȣ���Ȼ��
	 * @param num   ָ����ͣ��λ��ţ���Ȼ��
	 * @throws ���plate���Ѿ�ͣ�ڸ�ͣ����������num��λ�ѱ�������ռ�ã�����num��λ��Ȳ�����width������num�����ǺϷ���λ
	 */
	@Override
	public void parking(String plate, int width, int num, String type) throws Exception {

		Parkable car = Parkable.create(plate, width, type);

		// plate���Ѿ�ͣ�ڸ�ͣ����
		if (status.containsValue(car))
			throw new Exception();

		// num��λ�ѱ�������ռ��
		for (Lot lot : status.keySet())
			if (lot.getNumber() == num)
				throw new Exception();

		boolean legal = false;
		for (Lot lot : lots) {
			if (lot.getNumber() == num) {
				// num��λ��Ȳ�����width
				if (lot.getWidth() < width)
					throw new Exception();
				legal = true;

				// ��ʽͣ��
				status.put(lot, car);
				records.add(new Record(car, lot));
			}
		}
		// num�����ǺϷ���λ
		if (!legal)
			throw new Exception();

		checkRep();
	}

	@Override
	public void parking(String plate, int width, String type) {
		// TODO Auto-generated method stub
		checkRep();
	}

	@Override
	public double depart(String plate) throws Exception {
		// TODO Auto-generated method stub
		checkRep();
		return 0;
	}

	@Override
	public Map<Integer, String> status() {
		Map<Integer, String> st = new HashMap<>();
		for (Lot lot : lots) {
			Parkable c = status.get(lot);
			if (c == null)
				st.put(lot.getNumber(), "");
			else
				st.put(lot.getNumber(), status.get(lot).getPlate());
		}
		checkRep();
		return st;
	}

	@Override
	public int getNumberOfLots() {
		return lots.size();
	}

	@Override
	public boolean isLotInParkingField(int num, int width) {
		for (Lot lot : lots) {
			if (lot.getNumber() == num && lot.getWidth() == width)
				return true;
		}

		return false;
	}

	@Override
	public boolean isEmpty() {
		return status.isEmpty();
	}

	@Override
	public int getLotWidth(int num) throws Exception {
		for (Lot lot : lots) {
			if (lot.getNumber() == num)
				return lot.getWidth();
		}

		throw new Exception("No such lot " + num);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("The parking field has total number of lots: " + this.getNumberOfLots() + "\n");

		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(4);
		String ratio = numberFormat.format((double) this.status.size() / (double) this.getNumberOfLots() * 100);

		sb.append("Now " + ratio + "% lots are occupied" + "\n");
		for (Lot lot : lots) {
			if (status.get(lot) != null)
				sb.append("Lot " + lot.getNumber() + " (" + lot.getWidth() + "):\tCar " + status.get(lot).getPlate()
						+ "\n");
			else
				sb.append("Lot " + lot.getNumber() + " (" + lot.getWidth() + "):\tFree\n");

		}
		return sb.toString();
	}

}

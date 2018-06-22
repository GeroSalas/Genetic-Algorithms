package utn.ai.ag;

import java.io.Serializable;

public class Solution  implements Serializable {

	private Chromosome data;
	
	public Solution(Chromosome data) {
		super();
		this.data = data;
	}

	public Chromosome getData() {
		return data;
	}

	public void setData(Chromosome data) {
		this.data = data;
	}
	
}

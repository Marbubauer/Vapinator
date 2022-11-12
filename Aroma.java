public class Aroma {
	
	private String nome, gusto, marca;
	private int ml, ID;
	
	public Aroma(int ID, String nome, String gusto, String marca, int ml) {
		this.ID = ID;
		this.nome = nome;
		this.gusto = gusto;
		this.marca = marca;
		this.ml = ml;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getGusto() {
		return gusto;
	}

	public void setGusto(String gusto) {
		this.gusto = gusto;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public int getMl() {
		return ml;
	}

	public void setMl(int ml) {
		this.ml = ml;
	}

	@Override
	public String toString() {
		return "Aroma [nome=" + nome + ", gusto=" + gusto + ", marca=" + marca + ", ml=" + ml + ", ID=" + ID + "]";
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
	
	
	
}

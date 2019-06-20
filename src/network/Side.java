package network;

public enum Side {
	SERVER, CLIENT;
	public boolean isClient() {
		return this == CLIENT;
	}

	public boolean isServer() {
		return this == SERVER;
	}

	public String mark() {
		return this.isServer() ? "S" : "C";
	}
}

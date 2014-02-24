package org.equo.test.model.peer;

import org.equo.Domain;
import org.equo.Types;

public interface Domains {

	public static final Domain CCODE = new Domain("CCODE", Types.VARCHAR, 16, -1);
	public static final Domain ANAME = new Domain("ANAME", Types.VARCHAR, 16, -1);
	public static final Domain DDESC = new Domain("DDESC", Types.VARCHAR, 128, -1);
	public static final Domain FICON = new Domain("FICON", Types.VARCHAR, 64, -1);
	public static final Domain CLONGCODE = new Domain("CLONGCODE", Types.VARCHAR, 32, -1);
	public static final Domain ALONGNAME = new Domain("ALONGNAME", Types.VARCHAR, 32, -1);
	public static final Domain DLONGDESC = new Domain("DLONGDESC", Types.VARCHAR, 256, -1);
	public static final Domain NID = new Domain("NID", Types.INTEGER, 8, -1);
	public static final Domain NINT8 = new Domain("NINT8", Types.INTEGER, -1, -1);
	public static final Domain NORDER = new Domain("NORDER", Types.SMALLINT, -1, -1);
	public static final Domain NSECONDS = new Domain("NSECONDS", Types.SMALLINT, -1, -1);
	public static final Domain NCD = new Domain("NCD", Types.SMALLINT, -1, -1);
	public static final Domain NDAMAGE = new Domain("NDAMAGE", Types.SMALLINT, -1, -1);
	public static final Domain NAOE = new Domain("NAOE", Types.SMALLINT, -1, -1);
	public static final Domain NCOST = new Domain("NCOST", Types.SMALLINT, -1, -1);
	public static final Domain CORDER = new Domain("CORDER", Types.VARCHAR, 8, -1);
	public static final Domain CWEAPON = new Domain("CWEAPON", Types.VARCHAR, 2, -1);
	public static final Domain CWTYPE = new Domain("CWTYPE", Types.VARCHAR, 2, -1);
	public static final Domain CSTYPE = new Domain("CSTYPE", Types.VARCHAR, 1, -1);
	public static final Domain CSSTYPE = new Domain("CSSTYPE", Types.VARCHAR, 16, -1);
	public static final Domain ISTAT = new Domain("ISTAT", Types.VARCHAR, 1, -1);
	public static final Domain ICHAIN = new Domain("ICHAIN", Types.VARCHAR, 2, -1);
	public static final Domain CCOMBO = new Domain("CCOMBO", Types.VARCHAR, 4, -1);


	public static enum Weapon {

		Axe("AX"), 
		Dagger("DA"), 
		Mace("MA"), 
		Pistol("PI"), 
		Scepter("SC"), 
		Sword("SW"), 
		Focus("FO"), 
		Shield("SH"), 
		Torch("TO"), 
		Warhorn("WA"), 
		Greatsword("GS"), 
		Hammer("HA"), 
		Longbow("LB"), 
		Rifle("RI"), 
		Shortbow("SB"), 
		Staff("ST"), 
		HarpoonGun("HG"), 
		Spear("SP"), 
		Trident("TR");

		private String code;

		private Weapon(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}

	public static enum WeaponType {

		TwoHanded("2H"), 
		OneHand("1H"), 
		MainHand("MH"), 
		OffHand("OH"), 
		Aquatic("AQ");

		private String code;

		private WeaponType(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}

	public static enum SkillType {

		Weapon("W"), 
		Mechanic("M"), 
		Pet("P"), 
		Healing("H"), 
		Utility("U"), 
		Elite("E"), 
		Downed("D");

		private String code;

		private SkillType(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}

	public static enum StatType {

		Boon("B"), 
		Condition("C"), 
		Effect("E");

		private String code;

		private StatType(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}

	public static enum ChainType {

		Root_Chain_Skill("RC"), 
		Chain_Skill("CC"), 
		Root_Sequence_Skill("RS"), 
		Sequence_Skill("CC"), 
		Transformation_Skill("RF"), 
		Form_Skill("CF");

		private String code;

		private ChainType(String code) {
			this.code = code;
		}

		public String code() {
			return code;
		}
	}
}

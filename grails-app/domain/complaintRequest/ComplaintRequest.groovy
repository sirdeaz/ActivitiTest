package complaintRequest

class ComplaintRequest {

	String description
	String solution

	static constraints = { solution nullable: true }
}

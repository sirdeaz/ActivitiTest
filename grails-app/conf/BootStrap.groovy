import org.grails.activiti.identity.Role
import org.grails.activiti.identity.User
import org.grails.activiti.identity.UserRole

class BootStrap {

    def init = { servletContext ->
		def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
		def userRole = new Role(authority: 'ROLE_USER').save(flush: true)
  
		def testUser = new User(username: 'me', password: 'password')
		testUser.save(flush: true)
  
		UserRole.create testUser, adminRole, true
    }
    def destroy = {
    }
}

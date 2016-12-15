package main.java.com.db;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import main.java.com.aws.AWSConnectionManager;
import main.java.com.bean.User;
import main.java.com.bean.jpa.UserJPABean;

public class UserService {

	public List<User> fetchUsers() throws Exception {

		String query = "SELECT u FROM UserJPABean u";
		List<User> userList = fetchUser(query);

		return userList;

	}

	public User fetchUser(int id) throws Exception {

		String query = "SELECT u FROM UserJPABean u where u.id=" + id;
		List<User> userList = fetchUser(query);

		return userList.get(0);

	}

	private List<User> fetchUser(String query) throws Exception {
		List<UserJPABean> userList = new ArrayList<UserJPABean>();
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("persistenceUnit" );
		EntityManager entitymanager = emfactory.createEntityManager();

		Query jpaQuery = entitymanager.createQuery(query,UserJPABean.class);
		userList = jpaQuery.getResultList();
		List<User> list = copyToUserBean(userList);
		return list;
	}

	private List<User> copyToUserBean(List<UserJPABean> userList) {
		List<User> list = new ArrayList<User>();
		User user = null;
		for(UserJPABean bean : userList){
			user = new User();
			user.setId(bean.getId());
			user.setFullName(bean.getFullName());
			user.setAge(bean.getAge());
			user.setQualification(bean.getQualification());
			user.setYearPassed(bean.getYearPassed());
			user.setGender(bean.getGender());
			String interests = bean.getInterests();
			user.setInterests(interests.split(","));
			list.add(user);
		}
		return list;
	}

	public void updateUser(User user) throws Exception {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
		EntityManager em = emf.createEntityManager();
		UserJPABean jpaBean = em.find(UserJPABean.class, user.getId());
		
		
		em.getTransaction().begin();
		jpaBean.setFullName(user.getFullName());
		jpaBean.setAge(user.getAge());
		jpaBean.setQualification(user.getQualification());
		jpaBean.setYearPassed(user.getYearPassed());
		jpaBean.setGender(user.getGender());
		jpaBean.setInterests((Arrays.toString(user.getInterests())).substring(1, (Arrays.toString(user.getInterests()).length()-1)));
		em.persist(jpaBean);
		em.getTransaction().commit();
		em.close();

	}
	
	
	/**
	 * @param user
	 */
	private void persistUserData(User user) {
		UserJPABean jpaBean = new UserJPABean();
		jpaBean.setFullName(user.getFullName());
		jpaBean.setAge(user.getAge());
		jpaBean.setQualification(user.getQualification());
		jpaBean.setYearPassed(user.getYearPassed());
		jpaBean.setGender(user.getGender());
		jpaBean.setInterests((Arrays.toString(user.getInterests())).substring(1, (Arrays.toString(user.getInterests()).length()-1)));
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistenceUnit");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		em.persist(jpaBean);
		em.getTransaction().commit();
		em.close();
	}

	public void createUserTable() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = getMySqlConnection();
		
		String query="CREATE TABLE USERS(id INT NOT NULL PRIMARY KEY AUTO_INCREMENT, FULL_NAME VARCHAR(20), AGE int, QUALIFICATION VARCHAR(20), PASSED_YEAR VARCHAR(6),GENDER VARCHAR(6),INTERESTS VARCHAR(100))";
		
		Statement st = conn.createStatement();
		int rs = st.executeUpdate(query);
		System.out.println(rs);
	}
	public void insertIntoDataBase(User user) throws Exception {
		persistUserData(user);

	}

	public String getInterestList(String[] interests) {
		String list = Arrays.toString(interests);
		return list.substring(1, list.length() - 1);
	}

	public Connection getMySqlConnection() throws Exception {
		return AWSConnectionManager.getRemoteConnection();

	}

}

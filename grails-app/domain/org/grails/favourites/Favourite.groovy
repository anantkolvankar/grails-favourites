package org.grails.favourites

class Favourite {

	Long userId
	String userClass
	Long favouriteId
	String favouriteClass

	static constraints = {
		favouriteClass blank:false
		favouriteId min:0L
		userClass blank:false
		userId min:0L
	}

	def getFavourite() {
		// handle proxied class names
		if (favouriteClass.contains('_$$_javassist')) {
			favouriteClass -= '_$$_javassist'
		}
		getClass().classLoader.loadClass(favouriteClass).get(favouriteId)
	}

	def getUser() {
		// handle proxied class names
		if (userClass.contains('_$$_javassist')) {
			userClass -= '_$$_javassist'
		}
		getClass().classLoader.loadClass(userClass).get(userId)
	}
}

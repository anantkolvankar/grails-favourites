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
		def i = favouriteClass.indexOf('_$$_javassist')
		if(i>-1)
			favouriteClass = favouriteClass[0..i-1]
		getClass().classLoader.loadClass(favouriteClass).get(favouriteId)
	}


    def getUser() {
		// handle proxied class names
		def i = userClass.indexOf('_$$_javassist')
		if(i>-1)
			userClass = userClass[0..i-1]
		getClass().classLoader.loadClass(userClass).get(userId)
	}
}

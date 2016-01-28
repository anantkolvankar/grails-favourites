import grails.plugin.favourites.AddToFavourites
import grails.plugin.favourites.Favourite

class FavouriteGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "2.4 > *"
    def title = "Favourite Plugin"
    def author = "Anant Anil Kolvankar"
    def authorEmail = "ani.kolvankar@gmail.com"
    def description = 'Adds support for favourites. Mark up any of your domain classes as having favourites.'
    def documentation = "https://github.com/anantkolvankar/grails-favourites"
    def license = "APACHE"
    def issueManagement = [url: 'https://github.com/anantkolvankar/grails-favourites/issues']
    def scm = [ url: 'https://github.com/anantkolvankar/grails-favourites']

    def doWithDynamicMethods = { ctx ->
        for(domainClass in application.domainClasses) {
            if (!AddToFavourites.isAssignableFrom(domainClass.clazz)) {
                continue
            }

            domainClass.clazz.metaClass {

                addToFavouritesOf { user ->
                    if(delegate.id == null) throw println("You must save the entity [${delegate}] before calling addToFavouritesOf")

                    def userClass = user.class.name
                    if (userClass.contains('_$$_javassist')) {
                        userClass -= '_$$_javassist'
                    }

                    //check if delegate already added into favourites
                    def userId = user.id
                    def alreadyIntoFavourite=Favourite.find("from Favourite as f where f.userId=? and f.userClass=? and f.favouriteId=? and favouriteClass=?", [userId,userClass,delegate.id,delegate.class.name])

                    if(alreadyIntoFavourite){
                        println "Already Into Favourites"
                    }else{
                        def f = new Favourite(userId:user.id, userClass:userClass, favouriteId:delegate.id, favouriteClass:delegate.class.name)
                        if(!f.save()) {
                            throw println("Cannot create favourite for arguments $user, they are invalid.")
                        }
                    }
                    return delegate
                }

                getFavourites = {->
                    def instance = delegate
                    if(instance.id != null) {
                        return Favourite.findAll("from Favourite as f where f.userId=? and f.userClass=?", [instance.id,instance.class.name])
                    } else {
                        return Collections.EMPTY_LIST
                    }
                }

                removeFromFavouritesOf { user ->
                     if(delegate.id == null) throw println("You must save the entity [${delegate}] before calling removeFromFavouritesOf")
                     def userClass = user.class.name
                     if (favouriteClass.contains('_$$_javassist')) {
                         favouriteClass -= '_$$_javassist'
                     }

                    def userId = user.id

                    def favouriteToRemove=Favourite.find("from Favourite as f where f.userId=? and f.userClass=? and f.favouriteId=? and favouriteClass=?", [userId,userClass,delegate.id,delegate.class.name])

                    favouriteToRemove.delete()
                }
            }
        }
    }
}

import org.grails.favourites.*
import grails.util.*
class FavouriteGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.4 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Favourite Plugin" // Headline display name of the plugin
    def author = "Anant Anil Kolvankar"
    def authorEmail = "ani.kolvankar@gmail.com"
    def description = ''' 
    Adds support for favourites. Mark up any of your domain classes as having favourites.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/favourite"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
//    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        for(domainClass in application.domainClasses) {
            if(AddToFavourites.class.isAssignableFrom(domainClass.clazz)) {
                domainClass.clazz.metaClass {
                    
                    addToFavouritesOf { user ->
                            if(delegate.id == null) throw println("You must save the entity [${delegate}] before calling addToFavouritesOf")
                            
                            def userClass = user.class.name
                            def i = userClass.indexOf('_$$_javassist')
                            if(i>-1)
                                userClass = userClass[0..i-1]
                            
                            //check if delegate already added into favourites
                           def userId = user.id
                           def alreadyIntoFavourite=Favourite.find("from Favourite as f where f.userId=? and f.userClass=? and f.favouriteId=? and favouriteClass=?", [userId,userClass,delegate.id,delegate.class.name])

                            if(alreadyIntoFavourite){

                                println "Already Into Favourites"   
                            }else{

                                def f = new Favourite(userId:user.id, userClass:userClass, favouriteId:delegate.id, favouriteClass:delegate.class.name)
                                if(!f.validate()) {
                                    throw println("Cannot create favourite for arguments $user, they are invalid.")
                                }
                                f.save()
                                
                            }
                            return delegate
                    }
                    getFavourites = {->
                        def instance = delegate
                        if(instance.id != null) {
                            def allUserFavorites=Favourite.findAll("from Favourite as f where f.userId=? and f.userClass=?", [instance.id,instance.class.name])
                            return allUserFavorites
                        } else {
                            return Collections.EMPTY_LIST
                        }
                    }

                    removeFromFavouritesOf { user ->
                         if(delegate.id == null) throw println("You must save the entity [${delegate}] before calling removeFromFavouritesOf")
                         def userClass = user.class.name
                         def i = userClass.indexOf('_$$_javassist')
                         if(i>-1)
                            userClass = userClass[0..i-1]
                        
                        def userId = user.id

                        def favouriteToRemove=Favourite.find("from Favourite as f where f.userId=? and f.userClass=? and f.favouriteId=? and favouriteClass=?", [userId,userClass,delegate.id,delegate.class.name])

                        favouriteToRemove.delete()
                    }
                }
            }
        }    
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}

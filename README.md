# Favourites grails plugin
This plugin adds support for favourites. Mark up any of your domain classes as having favourites.
### Installation
```sh
grails install-plugin favourite
```
### Usage
Assume you have a product domain for which you want to add favorite feature.<br/>

Implement the ***AddToFavourites*** interface:<br/>

*Product.groovy*
```groovy
import grails.plugin.favourites.AddToFavourites

class Product implements AddToFavourites{
	String name
}
```

*User.groovy*
```groovy
import grails.plugin.favourites.AddToFavourites

class User implements AddToFavourites{
}
```
Add a particular product to user favorite list
```groovy
def product = Product.get(1)
def user = User.get(1)
product.addToFavouritesOf(user)
```
Remove product from user favourites

```groovy
  product.removeFromFavouritesOf(user)
```
Get all user favourites
```groovy
 user.favourites.each{
        println it.favourite.name
    }
```
>***Note:-*** ```it.favourite``` will be the instance of object that you have added in favourite, so in above case it is object of first product so you can  access all attributes of product like ```it.favourite.name ```

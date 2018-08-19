#import <Foundation/Foundation.h>

@class SWGApiClient;

/**
* Swagger Petstore *_/ ' \" =end -- \\r\\n \\n \\r
* This spec is mainly for testing Petstore server and contains fake endpoints, models. Please do not use this for any other purpose. Special characters: \" \\  *_/ ' \" =end --       
*
* OpenAPI spec version: 1.0.0 *_/ ' \" =end -- \\r\\n \\n \\r
* Contact: apiteam@swagger.io *_/ ' \" =end -- \\r\\n \\n \\r
*
* NOTE: This class is auto generated by the swagger code generator program.
* https://github.com/swagger-api/swagger-codegen.git
* Do not edit the class manually.
*/


@protocol SWGApi <NSObject>

@property(readonly, nonatomic, strong) SWGApiClient *apiClient;

-(instancetype) initWithApiClient:(SWGApiClient *)apiClient;

-(void) setDefaultHeaderValue:(NSString*) value forKey:(NSString*)key;
-(NSString*) defaultHeaderForKey:(NSString*)key;

-(NSDictionary *)defaultHeaders;

@end

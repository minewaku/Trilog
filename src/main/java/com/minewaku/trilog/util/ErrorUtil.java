package com.minewaku.trilog.util;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.minewaku.trilog.exception.ApiException;

@Component
public class ErrorUtil {
	
	public final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";
	public final String ACTION_NOT_ALLOWED = "ACTION_NOT_ALLOWED";
	public final String ACCESS_DENIED = "ACCESS_DENIDED";

	// Media-related errors
	public final String MEDIA_TYPE_UNSUPPORTED_IMAGE = "MEDIA_TYPE_UNSUPPORTED_IMAGE";
	public final String MEDIA_SIZE_EXCEEDED_IMAGE = "MEDIA_SIZE_EXCEEDED_IMAGE";
	public final String MEDIA_TYPE_UNSUPPORTED_VIDEO = "MEDIA_TYPE_UNSUPPORTED_VIDEO";
	public final String MEDIA_SIZE_EXCEEDED_VIDEO = "MEDIA_SIZE_EXCEEDED_VIDEO";
	public final String MEDIA_UPLOAD_ERROR = "MEDIA_UPLOAD_ERROR";
	public final String MEDIA_INITIALIZATION_ERROR = "MEDIA_INITIALIZATION_ERROR";

	public final String MEDIA_NOT_FOUND = "MEDIA_NOT_FOUND";
	public final String MEDIA_GET_ERROR = "MEDIA_GET_ERROR";
	public final String MEDIA_UPDATE_ERROR = "MEDIA_UPDATE_ERROR";
	public final String MEDIA_PATCH_ERROR = "MEDIA_PATCH_ERROR";
	public final String MEDIA_DELETE_ERROR = "MEDIA_DELETE_ERROR";

	// User-related errors
	public final String USER_NOT_FOUND = "USER_NOT_FOUND";
	public final String USER_GET_ERROR = "USER_GET_ERROR";
	public final String USER_UPDATE_ERROR = "USER_UPDATE_ERROR";
	public final String USER_PATCH_ERROR = "USER_PATCH_ERROR";
	public final String USER_DELETE_ERROR = "USER_DELETE_ERROR";

	// Permission-related errors
	public final String PERMISSION_NOT_FOUND = "PERMISSION_NOT_FOUND";
	public final String PERMISSION_GET_ERROR = "PERMISSION_GET_ERROR";
	public final String PERMISSION_UPDATE_ERROR = "PERMISSION_UPDATE_ERROR";
	public final String PERMISSION_PATCH_ERROR = "PERMISSION_PATCH_ERROR";
	public final String PERMISSION_DELETE_ERROR = "PERMISSION_DELETE_ERROR";

	// Role-related errors
	public final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";
	public final String ROLE_GET_ERROR = "ROLE_GET_ERROR";
	public final String ROLE_UPDATE_ERROR = "ROLE_UPDATE_ERROR";
	public final String ROLE_PATCH_ERROR = "ROLE_PATCH_ERROR";
	public final String ROLE_DELETE_ERROR = "ROLE_DELETE_ERROR";
	
	// Post-related errors
	public final String POST_NOT_FOUND = "POST_NOT_FOUND";
	public final String POST_GET_ERROR = "POST_GET_ERROR";
	public final String POST_UPDATE_ERROR = "POST_UPDATE_ERROR";
	public final String POST_PATCH_ERROR = "POST_PATCH_ERROR";
	public final String POST_DELETE_ERROR = "POST_DELETE_ERROR";
	
	// Comment-related errors
	public final String COMMENT_NOT_FOUND = "COMMENT_NOT_FOUND";
	public final String COMMENT_GET_ERROR = "COMMENT_GET_ERROR";
	public final String COMMENT_UPDATE_ERROR = "COMMENT_UPDATE_ERROR";
	public final String COMMENT_PATCH_ERROR = "COMMENT_PATCH_ERROR";
	public final String COMMENT_DELETE_ERROR = "COMMENT_DELETE_ERROR";
	
	//like-related errors
	public final String LIKE_POST_NOT_FOUND = "LIKE_POST_NOT_FOUND";
	public final String LIKE_POST_ALREADY_EXIST = "LIKE_POST_ALREADY_EXIST";
	
	//view-related errors
	public final String VIEW_POST_NOT_FOUND = "VIEW_POST_NOT_FOUND";
	public final String VIEW_POST_ALREADY_EXIST = "VIEW_POST_ALREADY_EXIST";

	// Account-related errors
	public final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";
	public final String ACCOUNT_NOT_VERIFIED_PENDING = "ACCOUNT_NOT_VERIFIED_PENDING";
	public final String ACCOUNT_NOT_VERIFIED = "ACCOUNT_NOT_VERIFIED";
	public final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

	// Registration-related errors
	public final String EMAIL_ALREADY_EXISTS = "EMAIL_ALREADY_EXISTS";
	public final String USERNAME_ALREADY_EXISTS = "USERNAME_ALREADY_EXISTS";
	
	// verify
	public final String EMAIL_VERIFICATION_INVALID_TOKEN = "EMAIL_VERIFICATION_INVALID_TOKEN";
	public final String EMAIL_VERIFICATION_EXPIRED_TOKEN = "EMAIL_VERIFICATION_EXPIRED_TOKEN";
	public final String UNABLE_SEND_EMAIL_VERIFICATION = "UNABLE_SEND_EMAIL_VERIFICATION";
	
	// Validation errors
	public final String INVALID_PARAMETERS = "INVALID_PARAMETERS";
	public final String INVALID_OPERATION = "INVALID_OPERATION";
	public final String CONSTRAINT_VIOLATION = "CONSTRAINT_VIOLATION";

	// Refresh token errors
	public final String REFRESH_TOKEN_EXPIRED = "REFRESH_TOKEN_EXPIRED";
	public final String INVALID_REFRESH_TOKEN = "INVALID_REFRESH_TOKEN";
	
	// Jwt token errors
	public final String INVALID_JWT_TOKEN = "INVALID_JWT_TOKEN";
	
	// request errors
	public final String REQUEST_IN_PROGRESS = "REQUEST_IN_PROGRESS";
	
	//Considering to switch into ConrrurentHashMap if this map might be modified in runtime for thread safety
	public final Map<String, ApiException> ERROR_DETAILS = Map.ofEntries(
			
		// FORBIDDEN ACTIONS
		Map.entry(ACTION_NOT_ALLOWED, new ApiException(
			MessageUtil.getMessage("action.not.allowed"), 
			ACTION_NOT_ALLOWED, 
			HttpStatus.FORBIDDEN)),
		
		Map.entry(ACCESS_DENIED, new ApiException(
			MessageUtil.getMessage("access.denied"),
			ACCESS_DENIED, 
			HttpStatus.FORBIDDEN)),
			
		// MEDIA ERRORS (GROUP THEM WITH VALIDATIONS LATTER)
		Map.entry(MEDIA_TYPE_UNSUPPORTED_IMAGE, new ApiException(
			MessageUtil.getMessage("cloudinary.invalid.media.type.image"), 
			MEDIA_TYPE_UNSUPPORTED_IMAGE,
			HttpStatus.UNSUPPORTED_MEDIA_TYPE)),
		
		Map.entry(MEDIA_SIZE_EXCEEDED_IMAGE, new ApiException(
			MessageUtil.getMessage("cloudinary.invalid.media.size.image"),
			MEDIA_SIZE_EXCEEDED_IMAGE,
			HttpStatus.PAYLOAD_TOO_LARGE)),
		
		Map.entry(MEDIA_TYPE_UNSUPPORTED_VIDEO, new ApiException(
            MessageUtil.getMessage("cloudinary.invalid.media.type.video"),
            MEDIA_TYPE_UNSUPPORTED_VIDEO,
            HttpStatus.UNSUPPORTED_MEDIA_TYPE)),
		
				
		Map.entry(MEDIA_SIZE_EXCEEDED_VIDEO, new ApiException(
            MessageUtil.getMessage("cloudinary.invalid.media.size.video"),
            MEDIA_SIZE_EXCEEDED_VIDEO,
            HttpStatus.PAYLOAD_TOO_LARGE)),
			
		Map.entry(MEDIA_UPLOAD_ERROR, new ApiException(
            MessageUtil.getMessage("cloudinary.upload.error"),
            MEDIA_UPLOAD_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
		
        Map.entry(MEDIA_INITIALIZATION_ERROR, new ApiException(
            MessageUtil.getMessage("cloudinary.initialization.error"),
            MEDIA_INITIALIZATION_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        //CRUD MEDIA ERRORS
        Map.entry(MEDIA_NOT_FOUND, new ApiException(
            MessageUtil.getMessage("media.not.found"),
            MEDIA_NOT_FOUND,
            HttpStatus.NOT_FOUND)),
        
        Map.entry(MEDIA_GET_ERROR, new ApiException(
        	MessageUtil.getMessage("media.get.error"),
        	MEDIA_GET_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(MEDIA_UPDATE_ERROR, new ApiException(
        	MessageUtil.getMessage("media.update.error"),
        	MEDIA_UPDATE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        Map.entry(MEDIA_PATCH_ERROR, new ApiException(
        	MessageUtil.getMessage("media.patch.error"),
        	MEDIA_PATCH_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(MEDIA_DELETE_ERROR, new ApiException(
        	MessageUtil.getMessage("media.delete.error"),
        	MEDIA_DELETE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),

        
      //CRUD USER ERRORS
        Map.entry(USER_NOT_FOUND, new ApiException(
            MessageUtil.getMessage("user.not.found"),
            USER_NOT_FOUND,
            HttpStatus.NOT_FOUND)),
        
        Map.entry(USER_GET_ERROR, new ApiException(
        	MessageUtil.getMessage("user.get.error"),
        	USER_GET_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(USER_UPDATE_ERROR, new ApiException(
        	MessageUtil.getMessage("user.update.error"),
        	USER_UPDATE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        Map.entry(USER_PATCH_ERROR, new ApiException(
        	MessageUtil.getMessage("user.patch.error"),
        	USER_PATCH_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(USER_DELETE_ERROR, new ApiException(
        	MessageUtil.getMessage("user.delete.error"),
        	USER_DELETE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        
      //CRUD ROLE ERRORS
        Map.entry(ROLE_NOT_FOUND, new ApiException(
            MessageUtil.getMessage("role.not.found"),
            ROLE_NOT_FOUND,
            HttpStatus.NOT_FOUND)),
        
        Map.entry(ROLE_GET_ERROR, new ApiException(
        	MessageUtil.getMessage("role.get.error"),
        	ROLE_GET_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(ROLE_UPDATE_ERROR, new ApiException(
        	MessageUtil.getMessage("role.update.error"),
        	ROLE_UPDATE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        Map.entry(ROLE_PATCH_ERROR, new ApiException(
        	MessageUtil.getMessage("role.patch.error"),
        	ROLE_PATCH_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(ROLE_DELETE_ERROR, new ApiException(
        	MessageUtil.getMessage("role.delete.error"),
        	ROLE_DELETE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        
        //CRUD POST ERRORS
        Map.entry(POST_NOT_FOUND, new ApiException(
            MessageUtil.getMessage("post.not.found"),
            POST_NOT_FOUND,
            HttpStatus.NOT_FOUND)),
        
        Map.entry(POST_GET_ERROR, new ApiException(
        	MessageUtil.getMessage("post.get.error"),
        	POST_GET_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(POST_UPDATE_ERROR, new ApiException(
        	MessageUtil.getMessage("post.update.error"),
        	POST_UPDATE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        Map.entry(POST_PATCH_ERROR, new ApiException(
        	MessageUtil.getMessage("post.patch.error"),
        	POST_PATCH_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(POST_DELETE_ERROR, new ApiException(
        	MessageUtil.getMessage("post.delete.error"),
        	POST_DELETE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        //CRUD COMMENT ERRORS
        Map.entry(COMMENT_NOT_FOUND, new ApiException(
            MessageUtil.getMessage("comment.not.found"),
            COMMENT_NOT_FOUND,
            HttpStatus.NOT_FOUND)),
        
        Map.entry(COMMENT_GET_ERROR, new ApiException(
        	MessageUtil.getMessage("comment.get.error"),
        	COMMENT_GET_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(COMMENT_UPDATE_ERROR, new ApiException(
        	MessageUtil.getMessage("comment.update.error"),
        	COMMENT_UPDATE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        Map.entry(COMMENT_PATCH_ERROR, new ApiException(
        	MessageUtil.getMessage("comment.patch.error"),
        	COMMENT_PATCH_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(COMMENT_DELETE_ERROR, new ApiException(
        	MessageUtil.getMessage("comment.delete.error"),
        	COMMENT_DELETE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        
      //CRUD PERMISSION ERRORS
        Map.entry(PERMISSION_NOT_FOUND, new ApiException(
            MessageUtil.getMessage("permission.not.found"),
            PERMISSION_NOT_FOUND,
            HttpStatus.NOT_FOUND)),
        
        Map.entry(PERMISSION_GET_ERROR, new ApiException(
        	MessageUtil.getMessage("permission.get.error"),
        	PERMISSION_GET_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(PERMISSION_UPDATE_ERROR, new ApiException(
        	MessageUtil.getMessage("permission.update.error"),
        	PERMISSION_UPDATE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
        Map.entry(PERMISSION_PATCH_ERROR, new ApiException(
        	MessageUtil.getMessage("permission.patch.error"),
        	PERMISSION_PATCH_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        		
        Map.entry(PERMISSION_DELETE_ERROR, new ApiException(
        	MessageUtil.getMessage("permission.delete.error"),
        	PERMISSION_DELETE_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
//		  LIKE_POST
		Map.entry(LIKE_POST_NOT_FOUND, new ApiException(
			MessageUtil.getMessage("like.post.not.found"), 
			LIKE_POST_NOT_FOUND, 
			HttpStatus.NOT_FOUND)),
		
		Map.entry(LIKE_POST_ALREADY_EXIST, new ApiException(
			MessageUtil.getMessage("like.post.already.exist"), 
			LIKE_POST_ALREADY_EXIST, 
			HttpStatus.BAD_REQUEST)),
        
//        VIEW_POST
		Map.entry(VIEW_POST_NOT_FOUND, new ApiException(
			MessageUtil.getMessage("view.post.not.found"), 
			VIEW_POST_NOT_FOUND, 
			HttpStatus.NOT_FOUND)),

		Map.entry(VIEW_POST_ALREADY_EXIST, new ApiException(
			MessageUtil.getMessage("view.post.already.exist"), 
			VIEW_POST_ALREADY_EXIST, 
			HttpStatus.BAD_REQUEST)),
        
//        LOGIN
        Map.entry(ACCOUNT_LOCKED, new ApiException(
        	MessageUtil.getMessage("login.error.account.locked"),
        	ACCOUNT_LOCKED,
            HttpStatus.FORBIDDEN)),
        
        Map.entry(ACCOUNT_NOT_VERIFIED_PENDING, new ApiException(
        	MessageUtil.getMessage("login.error.account.not.verified.pending"),
        	ACCOUNT_NOT_VERIFIED_PENDING,
            HttpStatus.BAD_REQUEST)),
        
        Map.entry(ACCOUNT_NOT_VERIFIED, new ApiException(
        	MessageUtil.getMessage("login.error.account.not.verified"),
        	ACCOUNT_NOT_VERIFIED,
            HttpStatus.UNAUTHORIZED)),
        
        Map.entry(INVALID_CREDENTIALS, new ApiException(
        	MessageUtil.getMessage("login.error.invalid.credentials"),
        	INVALID_CREDENTIALS,
            HttpStatus.UNAUTHORIZED)),
        
        
//        REGISTRATION
        Map.entry(EMAIL_ALREADY_EXISTS, new ApiException(
        	MessageUtil.getMessage("register.error.email.exists"),
        	EMAIL_ALREADY_EXISTS,
            HttpStatus.BAD_REQUEST)),
        
        Map.entry(USERNAME_ALREADY_EXISTS, new ApiException(
        	MessageUtil.getMessage("register.error.username.exists"),
        	USERNAME_ALREADY_EXISTS,
            HttpStatus.BAD_REQUEST)),
        
//      VEFIFICATION
        Map.entry(EMAIL_VERIFICATION_INVALID_TOKEN, new ApiException(
        	MessageUtil.getMessage("verify.email.error.invalid.token"),
        	EMAIL_VERIFICATION_INVALID_TOKEN,
            HttpStatus.BAD_REQUEST)),
        
        Map.entry(EMAIL_VERIFICATION_EXPIRED_TOKEN, new ApiException(
			MessageUtil.getMessage("verify.email.error.expired.token"), 
			EMAIL_VERIFICATION_EXPIRED_TOKEN, 
			HttpStatus.BAD_REQUEST)),
        
        Map.entry(UNABLE_SEND_EMAIL_VERIFICATION, new ApiException(
        	MessageUtil.getMessage("verify.email.error.sent"),
        	UNABLE_SEND_EMAIL_VERIFICATION,
            HttpStatus.INTERNAL_SERVER_ERROR)),
        
//      VALIDATION ERRORS
        Map.entry(INVALID_PARAMETERS, new ApiException(
        	MessageUtil.getMessage("invalid.parameters"),
        	INVALID_PARAMETERS,
            HttpStatus.BAD_REQUEST)),
        
        Map.entry(INVALID_OPERATION, new ApiException(
            	MessageUtil.getMessage("invalid.operation"),
            	INVALID_OPERATION,
                HttpStatus.BAD_REQUEST)),
        
        Map.entry(CONSTRAINT_VIOLATION, new ApiException(
			MessageUtil.getMessage("constraint.violation"), 
			CONSTRAINT_VIOLATION,
			HttpStatus.BAD_REQUEST)),
        
        
// 		REFRESH TOKEN        
        Map.entry(REFRESH_TOKEN_EXPIRED, new ApiException(
        	MessageUtil.getMessage("refresh.token.expired"),
        	REFRESH_TOKEN_EXPIRED,
            HttpStatus.BAD_REQUEST)),
        
        Map.entry(INVALID_REFRESH_TOKEN, new ApiException(
        	MessageUtil.getMessage("refresh.token.invalid"),
        	INVALID_REFRESH_TOKEN,
            HttpStatus.BAD_REQUEST)),
        
//        JWT TOKEN
        Map.entry(INVALID_JWT_TOKEN, new ApiException(
        	MessageUtil.getMessage("jwt.token.invalid"),
        	INVALID_JWT_TOKEN,
            HttpStatus.UNAUTHORIZED)),
 
//		  REQUEST
        Map.entry(REQUEST_IN_PROGRESS, new ApiException(
        	MessageUtil.getMessage("request.in.progress"),
        	REQUEST_IN_PROGRESS,
            HttpStatus.LOCKED))
	);
}

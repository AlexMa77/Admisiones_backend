package com.gestion.educativa.data.repository;

import com.gestion.educativa.data.api.ApiService;
import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.preferences.UserPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class AuthRepository_Factory implements Factory<AuthRepository> {
  private final Provider<ApiService> apiProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<UserPreferences> prefsProvider;

  public AuthRepository_Factory(Provider<ApiService> apiProvider,
      Provider<TokenManager> tokenManagerProvider, Provider<UserPreferences> prefsProvider) {
    this.apiProvider = apiProvider;
    this.tokenManagerProvider = tokenManagerProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public AuthRepository get() {
    return newInstance(apiProvider.get(), tokenManagerProvider.get(), prefsProvider.get());
  }

  public static AuthRepository_Factory create(Provider<ApiService> apiProvider,
      Provider<TokenManager> tokenManagerProvider, Provider<UserPreferences> prefsProvider) {
    return new AuthRepository_Factory(apiProvider, tokenManagerProvider, prefsProvider);
  }

  public static AuthRepository newInstance(ApiService api, TokenManager tokenManager,
      UserPreferences prefs) {
    return new AuthRepository(api, tokenManager, prefs);
  }
}

package com.gestion.educativa.ui.auth;

import com.gestion.educativa.data.api.TokenManager;
import com.gestion.educativa.data.preferences.UserPreferences;
import com.gestion.educativa.data.repository.AuthRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AuthViewModel_Factory implements Factory<AuthViewModel> {
  private final Provider<AuthRepository> repositoryProvider;

  private final Provider<TokenManager> tokenManagerProvider;

  private final Provider<UserPreferences> prefsProvider;

  public AuthViewModel_Factory(Provider<AuthRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider, Provider<UserPreferences> prefsProvider) {
    this.repositoryProvider = repositoryProvider;
    this.tokenManagerProvider = tokenManagerProvider;
    this.prefsProvider = prefsProvider;
  }

  @Override
  public AuthViewModel get() {
    return newInstance(repositoryProvider.get(), tokenManagerProvider.get(), prefsProvider.get());
  }

  public static AuthViewModel_Factory create(Provider<AuthRepository> repositoryProvider,
      Provider<TokenManager> tokenManagerProvider, Provider<UserPreferences> prefsProvider) {
    return new AuthViewModel_Factory(repositoryProvider, tokenManagerProvider, prefsProvider);
  }

  public static AuthViewModel newInstance(AuthRepository repository, TokenManager tokenManager,
      UserPreferences prefs) {
    return new AuthViewModel(repository, tokenManager, prefs);
  }
}

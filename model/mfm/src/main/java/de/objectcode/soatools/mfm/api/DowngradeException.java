package de.objectcode.soatools.mfm.api;

public class DowngradeException extends Exception
{
  private static final long serialVersionUID = -7381099619138797074L;

  public DowngradeException(String message)
  {
    super(message);
  }

  public DowngradeException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public DowngradeException(Throwable cause)
  {
    super(cause);
  }
}

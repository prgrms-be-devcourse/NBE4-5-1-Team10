import ClientPage from "./client-page";
import RequireAuthenticated from "@/components/auth/require-autenticated"; // 이동이 필요할 경우

export default function Page() {
  return (
    <RequireAuthenticated>
      <ClientPage />
    </RequireAuthenticated>
  );
}